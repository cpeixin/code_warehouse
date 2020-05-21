package data_stream

import java.util.Properties

import org.apache.flink.streaming.api.scala._
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase
import org.apache.flink.util.Collector
import utils.KafkaUtil

object sideoutput_datastream {

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
        val date_time: String = get_value(x)._1.replace("2020-05-13","2018-04-20")
        val keywordList: String = get_value(x)._2
        Raw(date_time, keywordList)
      })

    val processStream: DataStream[Raw] = word_stream.process(new SideOutput())

    processStream.print("original data")
    //通过getSideOutput获取侧输出流，并打印输出
    processStream.getSideOutput(new OutputTag[String]("dirty_data")).print("side output data")

    env.execute("side output test")

  }

  def get_value(string_data: String): (String, String) = {
    val json_data: JSONObject = JSON.parseObject(string_data)
    val date_time: String = json_data.get("datetime").toString
    val keywordList: String = json_data.get("keywordList").toString
    (date_time, keywordList)
  }

  class SideOutput() extends ProcessFunction[Raw, Raw] {
    //定义一个侧输出流标签
    lazy val dirty_data: OutputTag[String] = new OutputTag[String]("dirty_data")

    override def processElement(value: Raw,
                                ctx: ProcessFunction[Raw, Raw]#Context,
                                out: Collector[Raw]): Unit = {
      if (value.keywordList == "dirty_data") {
        ctx.output(dirty_data, s"${value.date_time}+${value.keywordList}  is dirty data")
      } else {
        out.collect(value)
      }
    }
  }

}
