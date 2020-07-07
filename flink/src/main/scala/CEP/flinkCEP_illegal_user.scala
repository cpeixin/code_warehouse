package CEP

import CEP.flinkCEP_illegal_user.UserBehavior
import org.apache.flink.cep.scala.pattern.Pattern
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
object flinkCEP_illegal_user {

  case class UserBehavior(user_id: String, user_name: String, page_url: String, data_time: Long, ip_adress: String)

  def main(args: Array[String]): Unit = {
    val stream_env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    stream_env.setParallelism(1)
    stream_env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val user_behavior_stream: DataStream[UserBehavior] = stream_env.socketTextStream("localhost", 8888).map((line: String) => {
      val array_line: Array[String] = line.split(",")
      UserBehavior(array_line(0), array_line(1), array_line(2), array_line(3).toLong, array_line(4))
    })
      .assignAscendingTimestamps((_: UserBehavior).data_time)//设置时间语义,此方法是数据流的快捷方式，其中已知元素时间戳在每个并行流中单调递增。在这种情况下，系统可以通过跟踪上升时间戳自动且完美地生成水印。

    Pattern
      .begin[UserBehavior]("search illegal user")
      .where((_: UserBehavior).ip_adress.startsWith(""))
      .next("")
      .where((_: UserBehavior).ip_adress.startsWith(""))
      .next("")
      .where((_: UserBehavior).ip_adress.startsWith(""))
      .within(Time.seconds(60))
  }
}
