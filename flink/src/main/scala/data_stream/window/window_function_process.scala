package data_stream.window
import java.text.SimpleDateFormat
import java.util.{Date, Properties}
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase
import org.apache.flink.util.Collector
import utils.KafkaUtil

object window_function_process {

  case class UserLogData(real_name: String, user_name: String, age: Int, sex: String, phone_number: String,
                         province: String, email: String, ip_address: String, company: String, channel: String,
                         device: String, register_date_time: String, last_login_time: String)

  private val KAFKA_TOPIC: String = "user_information"

  def main(args: Array[String]) {

    val properties: Properties = new Properties()
    properties.setProperty("bootstrap.servers", "localhost:9092")
    properties.setProperty("group.id", "kafka_consumer")

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

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
      * 这里是使用ProcessWindowFunction底层函数来做处理
      * 为了清楚的显示，在每个窗口上打印了时间。
      */

    val original_format_stream: DataStream[((String, String), Int)] = original_stream
      .map(match_data(_: String))
      .map((raw: UserLogData) => ((raw.channel, raw.device), 1))


    original_format_stream
      .keyBy((_: ((String, String), Int))._1)
      .timeWindow(Time.seconds(10), Time.seconds(5))
      .process(new ProcessWindowFunction[((String, String), Int), ((String, String), Long), (String, String), TimeWindow] {
        override def process(key: (String, String), context: Context, elements: Iterable[((String, String), Int)], out: Collector[((String, String), Long)]): Unit = {
          val process_time: String = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date)
          println(s"=========$process_time=============")
          out.collect((key, elements.size))
        }
      }).print("processFunctionWindow")
    env.execute("process function stream")
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
