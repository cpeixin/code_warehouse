package data_stream.source_sink

import java.util.{Date, Properties}

import com.alibaba.fastjson.JSON
import org.apache.flink.streaming.connectors.kafka._
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests
import utils.KafkaUtil


object datastream_2_es_1 {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    // 非常关键，一定要设置启动检查点！！
    env.enableCheckpointing(5000)

    //配置kafka信息
    val props = new Properties()
    props.setProperty("bootstrap.servers", "192.168.199.128:9092,192.168.199.131:9092,192.168.199.132:9092")
    props.setProperty("zookeeper.connect", "192.168.199.128:2181,192.168.199.131:2181,192.168.199.132:2181")
    props.setProperty("group.id", "test")
    //读取数据
    val consumer = KafkaUtil.getKafkaSource("")
    //设置只读取最新数据
    consumer.setStartFromLatest()
    //添加kafka为数据源
    //18542360152   116.410588, 39.880172   2019-05-24 23:43:38
    val stream = env.addSource(consumer).map(
      x=>{
        JSON.parseObject(x)
      }
    ).map(x=>{
      x.getString("message")
    }).map(x=>{
      val jingwei=x.split("\\t")(1)
      val wei=jingwei.split(",")(0).trim
      val jing=jingwei.split(",")(1).trim
      val time=new Date().getTime
      val resultStr=wei+","+jing+","+time
      resultStr
    })

    stream.print()

    val httpHosts = new java.util.ArrayList[HttpHost]
    httpHosts.add(new HttpHost("192.168.199.128", 9200, "http"))

    val esSinkBuilder = new ElasticsearchSink.Builder[String](
      httpHosts,
      new ElasticsearchSinkFunction[String]{
        def createIndexRequest(element: String):IndexRequest={
          val json = new java.util.HashMap[String, String]
          json.put("wei", element.split(",")(0))
          json.put("jing", element.split(",")(1))
          json.put("time", element.split(",")(2))

          return Requests.indexRequest()
            .index("location-index")
            .`type`("location")
            .source(json)
        }

        override def process(element: String, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
          requestIndexer.add(createIndexRequest(element))
        }
      }
    )

    //批量请求的配置；这将指示接收器在每个元素之后发出请求，否则将对它们进行缓冲。
    esSinkBuilder.setBulkFlushMaxActions(1)

    stream.addSink(esSinkBuilder.build())

    env.execute("Kafka_Flink")
  }
}