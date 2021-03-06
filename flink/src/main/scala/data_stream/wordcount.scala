package data_stream

import java.util.Properties
import org.apache.flink.streaming.api.scala._
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase
import utils.KafkaUtil

object wordcount {

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

    //    持续流  数据累加
    val word_stream: DataStream[(String, Int)] = env.addSource(kafkaSource)
      .flatMap(get_value(_: String).split(","))
      .map((_: String, 1))
      .keyBy(0)
      .sum(1)

    word_stream.print("cpeixin").setParallelism(1)



    // execute program
    env.execute("Flink Streaming—————KafkaSource")
  }

  def get_value(string_data: String): String = {
    val json_data: JSONObject = JSON.parseObject(string_data)
    val date_time: String = json_data.get("datetime").toString
    val keywordList: String = json_data.get("keywordList").toString
    keywordList
  }

}
