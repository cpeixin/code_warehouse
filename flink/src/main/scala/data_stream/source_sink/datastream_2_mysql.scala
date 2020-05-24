package data_stream.source_sink

import java.util.Properties

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase
import utils.KafkaUtil


object datastream_2_mysql {

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

    val sink = new MysqlSink_v2()
    word_stream.addSink(sink)

    env.execute("Flink Streaming—————mysql sink")
  }
  def get_value(string_data: String): (String, String) = {
    val json_data: JSONObject = JSON.parseObject(string_data)
    val date_time: String = json_data.get("datetime").toString
    val keywordList: String = json_data.get("keywordList").toString
    (date_time, keywordList)
  }
}
