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

    val properties: Properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "kafka_consumer_hbase")

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    env.enableCheckpointing(5000)
    env.setStateBackend(new FsStateBackend("file:///Users/cpeixin/IdeaProjects/code_warehouse/data/KafkaSource/hbase"))


    val topic: String = "weibo_keyword"
    val kafkaSource: FlinkKafkaConsumerBase[String] = KafkaUtil.getKafkaSource(topic).setStartFromLatest()


    val word_stream: DataStream[Raw] = env.addSource(kafkaSource)(createTypeInformation)
      .map((x: String) => {
        val date_time: String = datastream_2_hbase.get_value(x)._1
        val keywordList: String = datastream_2_hbase.get_value(x)._2
        Raw.apply(date_time, keywordList)
      })(createTypeInformation)

    

    val value: DataStreamSink[Raw] = word_stream.addSink(new HBaseSink_v4("t_weibo_keyword_2","cf1")).name("write_2_hbase")
//    word_stream.writeUsingOutputFormat(new HBaseOutputFormat)

    env.execute("Flink Streaming—————hbase sink")
  }
  def get_value(string_data: String): Tuple2[String, String] = {
    val json_data: JSONObject = JSON.parseObject(string_data)
    val date_time: String = json_data.get("datetime").toString
    val keywordList: String = json_data.get("keywordList").toString
    return Tuple2.apply(date_time, keywordList)
  }
}
