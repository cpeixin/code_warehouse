package data_stream.source_sink

import java.util.Properties

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.datastream.DataStreamSink
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase
import utils.KafkaUtil


object datastream_2_hbase {

  case class Raw(date_time: String, keywordList: String)

  private val KAFKA_TOPIC: String = "weibo_keyword"


  def main(args: Array[String]): Unit = {

    // kafka 参数
    val properties: Properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "kafka_consumer_hbase")



    //创建上下文，与Spark中的SparkSession类似
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 每隔1000 ms进行启动一个检查点
    env.enableCheckpointing(1000)
    // 高级选项：
    // 设置模式为exactly-once （这是默认值）
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    // 确保检查点之间有进行500 ms的进度
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
    // 检查点必须在一分钟内完成，或者被丢弃
    env.getCheckpointConfig.setCheckpointTimeout(60000)
    // 同一时间只允许进行一个检查点
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
    // 选择状态的后端存储
    env.setStateBackend(new FsStateBackend("file:///Users/cpeixin/IdeaProjects/code_warehouse/data/KafkaSource/hbase"))


    val topic: String = "weibo_keyword"
    val kafkaSource: FlinkKafkaConsumerBase[String] = KafkaUtil.getKafkaSource(topic).setStartFromLatest()


    // Source
    val word_stream: DataStream[Raw] = env.addSource(kafkaSource)(createTypeInformation)
      .map((x: String) => {
        val date_time: String = datastream_2_hbase.get_value(x)._1
        val keywordList: String = datastream_2_hbase.get_value(x)._2
        Raw.apply(date_time, keywordList)
      })(createTypeInformation)




    val value: DataStreamSink[Raw] = word_stream.addSink(new HBaseSink_v4("t_weibo_keyword_2","cf1")).name("write_2_hbase")

    env.execute("Flink Streaming hbase sink")
  }




  def get_value(string_data: String): Tuple2[String, String] = {
    val json_data: JSONObject = JSON.parseObject(string_data)
    val date_time: String = json_data.get("datetime").toString
    val keywordList: String = json_data.get("keywordList").toString
    Tuple2.apply(date_time, keywordList)
  }
}
