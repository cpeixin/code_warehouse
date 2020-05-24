package data_stream.window

import java.util.Properties

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase
import utils.KafkaUtil

/**
  * reduce Function
  * 增量累加
  * 输入输出类型要一致
  */
object window_function_reduce {

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
      * 这里我们设定一个具体的需求，统计过去的5秒内，各省份访问的用户数量统计
      * 下面第二次map操作=》(String, Int)，是因为接下来要使用reduce函数
      * reduce函数聚合是要求输入和输出格式相同的。
      *
      * 以下的转换步骤可以串联的写在一起，但是在这里分开写是想清晰的展示每一步转换后，Stream的类型变换
      */

    val original_format_stream: DataStream[(String, Int)] = original_stream
      .map(match_data(_: String))
      .map((raw: UserLogData) =>(raw.province, 1))


    val province_window_stream: WindowedStream[(String, Int), String, TimeWindow] = original_format_stream
      .keyBy((_: (String, Int))._1)
      .timeWindow(Time.seconds(5))


    val result_stream: DataStream[(String, Int)] = province_window_stream
      .reduce((p1: (String, Int), p2: (String, Int)) => (p1._1, p1._2 + p2._2))


    result_stream.print("province_count").setParallelism(1)

    env.execute("province count stream")

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
