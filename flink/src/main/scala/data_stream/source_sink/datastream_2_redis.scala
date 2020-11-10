package data_stream.source_sink

import java.util.Properties

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase
import org.apache.flink.streaming.connectors.redis.RedisSink
import utils.{KafkaUtil, RedisUtil}


object datastream_2_redis {

  case class Raw(date_time: String, keywordList: String)
  private val KAFKA_TOPIC: String = "weibo_keyword"

  def main(args: Array[String]) {

    // kafka 参数
    val properties: Properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "kafka_consumer")

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // exactly-once 语义保证整个应用内端到端的数据一致性
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    // 开启检查点并指定检查点时间间隔为5s
    env.enableCheckpointing(5000) // checkpoint every 5000 msecs

    // 高级选项：
    // 确保检查点之间有进行500 ms的进度
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    // 检查点必须在一分钟内完成，或者被丢弃
    env.getCheckpointConfig.setCheckpointTimeout(60000)
    // 同一时间只允许进行一个检查点
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    // 设置StateBackend，并指定状态数据存储位置
    env.setStateBackend(new FsStateBackend("file:///Users/cpeixin/IdeaProjects/code_warehouse/data/KafkaSource"))


    // Source
    val topic: String = "weibo_keyword"
    val kafkaSource: FlinkKafkaConsumerBase[String] = KafkaUtil.getKafkaSource(topic)

    // Process
    val word_stream: DataStream[Raw] = env.addSource(kafkaSource)
      .map((x: String) => {
        val date_time: String = get_value(x)._1.replace("2020-05-22","2018-04-30")
        val keywordList: String = get_value(x)._2
        Raw(date_time, keywordList)
      })


    word_stream.print("2_redis").setParallelism(1)

    // Sink
    val redis_sink: RedisSink[Raw] = RedisUtil.getRedisSink()
    word_stream.addSink(redis_sink).name("write_2_redis")

    // Over
    env.execute("Flink Streaming redis sink")
  }


  // 自定义函数
  def get_value(string_data: String): (String, String) = {
    val json_data: JSONObject = JSON.parseObject(string_data)
    val date_time: String = json_data.get("datetime").toString
    val keywordList: String = json_data.get("keywordList").toString
    (date_time, keywordList)
  }
}
