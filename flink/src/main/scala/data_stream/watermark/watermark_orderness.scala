package data_stream.watermark
import java.text.SimpleDateFormat
import java.util.Date

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object watermark_orderness {

   case class game_gateway_data(user_id: String, step_num: String, gateway_time: Long, gateway_name: String, delay_time: Int)

   def main(args: Array[String]): Unit = {
     val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

     env.setParallelism(1)

     env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

     val socketStream: DataStream[String] = env.socketTextStream("localhost",8888)

     val gameGatewayStream: DataStream[game_gateway_data] = socketStream.map((line: String) => {
       val array_data: Array[String] = line.split(",")
       game_gateway_data(array_data(0), array_data(1), array_data(2).toLong, array_data(3), array_data(4).toInt)
     })
     //设置时间语义
     .assignAscendingTimestamps((_: game_gateway_data).gateway_time)


     gameGatewayStream
       .keyBy((_: game_gateway_data).user_id)
       .timeWindow(Time.seconds(10), Time.seconds(5))
       .reduce(new MyReduceFunction, new stepTimeWindowFunction)
       .print("====>")

     env.execute("orderness case")

   }


  class MyReduceFunction extends ReduceFunction[game_gateway_data]{
    override def reduce(t: game_gateway_data, t1: game_gateway_data): game_gateway_data = {
      if (t.delay_time >= t1.delay_time) t else t1
    }
  }

  class stepTimeWindowFunction extends WindowFunction[game_gateway_data, game_gateway_data, String, TimeWindow]{
    // 在窗口结束时调用
    override def apply(key: String, window: TimeWindow, input: Iterable[game_gateway_data], out: Collector[game_gateway_data]): Unit = {
    // input中只有一条数据
      println(s"${window.getStart}===${window.getEnd}")
      val result: game_gateway_data = input.iterator.next()
      out.collect(result)
    }
  }


}


