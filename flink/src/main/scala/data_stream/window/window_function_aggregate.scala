package data_stream.window

import java.util.Properties

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, WindowedStream}
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase
import org.apache.flink.util.Collector
import utils.KafkaUtil

object window_function_aggregate {


  class aggregate_channel_devices extends AggregateFunction[((String,String), Int),Long, Long]{
    // 数据累加逻辑
    override def add(in: ((String, String), Int), acc: Long): Long = acc + in._2
    // 累加器初始化
    override def createAccumulator(): Long = 0L
    // 累加器计算的结果
    override def getResult(acc: Long): Long = acc
    // 不同分区之间，累加器的值相加
    override def merge(acc: Long, acc1: Long): Long = acc + acc1
  }

  /**
    * 作用与窗口内，aggregate后数据的函数
    * 基于接口WindowFunction[IN, OUT, KEY, W <: Window]
    * IN ：输入参数，为 AggregateFunction的输出
    * OUT：输出参数
    *
    */
  class channel_devices_windowFuction extends WindowFunction[Long, ((String, String), Long), (String, String), TimeWindow]{
    override def apply(key: (String, String), window: TimeWindow, input: Iterable[Long], out: Collector[((String, String), Long)]): Unit = {
      out.collect((key, input.iterator.next()))
    }
  }


  case class UserLogData(real_name: String, user_name: String, age: Int, sex: String, phone_number: String,
                         province: String, email: String, ip_address: String, company: String, channel: String,
                         device: String, register_date_time: String, last_login_time: String)

  private val KAFKA_TOPIC: String = "user_information"

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
    env.setStateBackend(new FsStateBackend("file:///Users/cpeixin/IdeaProjects/code_warehouse/data/KafkaSource/tumbling"))

    val kafkaSource: FlinkKafkaConsumerBase[String] = KafkaUtil.getKafkaSource(KAFKA_TOPIC)

    //计算窗口内5秒的数据
    val original_stream: DataStream[String] = env.addSource(kafkaSource)

    """
      |original_stream> UserLogData(纪淑兰,ehao,37,female,18529008264,辽宁省,apeng@gmail.com,110.75.179.140,易动力科技有限公司,app,xiaomi,2017-08-18 12:29:29,2018-09-02 05:05:56)
      |original_stream> UserLogData(刘玉英,caojing,42,male,15647635274,香港特别行政区,houwei@hotmail.com,50.188.173.136,同兴万点传媒有限公司,web,Firefox,2017-10-27 12:20:20,2018-10-11 09:26:20)
      |original_stream> UserLogData(薛萍,leizhong,32,male,18928404956,云南省,xxiao@yahoo.com,115.123.146.193,网新恒天传媒有限公司,web,360_browser,2017-06-17 16:42:24,2018-09-19 19:36:39)
    """.stripMargin


    /**
      * 这里我们设定一个具体的需求，每5秒统计过去10秒中，用户渠道来源以及使用设备的排行
      * 下面第二次map操作=》(String, Int)，是因为接下来要使用reduce函数
      * reduce函数聚合是要求输入和输出格式相同的。
      *
      * 以下的转换步骤可以串联的写在一起，但是在这里分开写是想清晰的展示每一步转换后，Stream的类型变换
      */

    val original_format_stream: DataStream[((String, String),Int)] = original_stream
      .map(match_data(_: String))
      .map((raw: UserLogData) => ((raw.channel, raw.device), 1))


    original_format_stream
      .keyBy((_: ((String, String), Int))._1)
      .window(SlidingProcessingTimeWindows.of(Time.seconds(10),Time.seconds(5)))
      .aggregate(new aggregate_channel_devices, new channel_devices_windowFuction)
      .print("channel_device")


    env.execute("channel_device stream")

  }


  def match_data(original_str: String): UserLogData = {
    val original_json: JSONObject = JSON.parseObject(original_str)
    UserLogData(original_json.getString("real_name"), original_json.getString("user_name"), original_json.getInteger("age"),
      original_json.getString("sex"), original_json.getString("phone_number"), original_json.getString("province"),
      original_json.getString("email"), original_json.getString("ip_address"), original_json.getString("company"),
      original_json.getString("channel"), original_json.getString("device"), original_json.getString("register_date_time"),
      original_json.getString("last_login_time"))
  }





}
