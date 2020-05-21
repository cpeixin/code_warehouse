package data_stream

import java.util
import java.util.Properties

import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests
import utils.KafkaUtil
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.elasticsearch.{ActionRequestFailureHandler, ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase
import org.elasticsearch.action.ActionRequest


object datastream_2_es {

  case class Raw(date_time: String, keywordList: String)

  private val KAFKA_TOPIC: String = "weibo_keyword"

  def main(args: Array[String]) {

    val properties: Properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "kafka_consumer")

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // exactly-once 语义保证整个应用内端到端的数据一致性
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    // 开启检查点并指定检查点时间间隔为5s
    env.enableCheckpointing(5000) // checkpoint every 5000 msecs
    // 设置StateBackend，并指定状态数据存储位置
    env.setStateBackend(new FsStateBackend("file:///Users/cpeixin/IdeaProjects/code_warehouse/data/KafkaSource"))


    val topic: String = "weibo_keyword"
    val kafkaSource: FlinkKafkaConsumerBase[String] = KafkaUtil.getKafkaSource(topic)

    val word_stream: DataStream[Raw] = env.addSource(kafkaSource)
      .map((x: String) => {
        val date_time: String = get_value(x)._1.replace("2020-05-17","2018-04-30")
        val keywordList: String = get_value(x)._2
        Raw(date_time, keywordList)
      })

    val httpHosts = new java.util.ArrayList[HttpHost]
    httpHosts.add(new HttpHost("127.0.0.1", 9200, "http"))


    val esSinkBuilder = new ElasticsearchSink.Builder[Raw] (
      httpHosts, new ElasticsearchSinkFunction[Raw] {
        override def process(data: Raw, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          print("saving data" + data)
          //包装成一个Map或者JsonObject
          val hashMap = new util.HashMap[String, String]()
          hashMap.put("date_time", data.date_time)
          hashMap.put("keyword_list", data.keywordList)
          hashMap.put("rowkey", "i am rowkey haha")
          //创建index request,准备发送数据
          val indexRequest: IndexRequest = Requests.indexRequest().index("weibo_keyword-2018-04-30").`type`("default").source(hashMap)
          //发送请求,写入数据
          requestIndexer.add(indexRequest)
          println("data saved successfully")
        }
      }
    )


    esSinkBuilder.setBulkFlushMaxActions(2)
    esSinkBuilder.setBulkFlushInterval(1000L)
    // 自定义异常处理
    esSinkBuilder.setFailureHandler(new ActionRequestFailureHandler {
      override def onFailure(actionRequest: ActionRequest, throwable: Throwable, i: Int, requestIndexer: RequestIndexer): Unit = {
        println("@@@@@@@On failure from ElasticsearchSink:-->" + throwable.getMessage)
      }
    })

    word_stream.addSink(esSinkBuilder.build())

    env.execute("Flink Streaming—————es sink")
  }
  def get_value(string_data: String): (String, String) = {
    val json_data: JSONObject = JSON.parseObject(string_data)
    val date_time: String = json_data.get("datetime").toString
    val keywordList: String = json_data.get("keywordList").toString
    (date_time, keywordList)
  }
}
