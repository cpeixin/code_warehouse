package data_stream.watermark

import org.apache.flink.api.common.functions.ReduceFunction
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

object watermark_outoforderness {

  case class game_gateway_data(user_id: String, step_num: String, gateway_time: Long, gateway_name: String, delay_time: Int)

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)

    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    env.getConfig.setAutoWatermarkInterval(100L) //watermark周期

    val socketStream: DataStream[String] = env.socketTextStream("localhost", 8888)

    val gameGatewayStream: DataStream[game_gateway_data] = socketStream.map((line: String) => {
      val array_data: Array[String] = line.split(",")
      game_gateway_data(array_data(0), array_data(1), array_data(2).toLong, array_data(3), array_data(4).toInt)
    })
      //乱序数据处理，采用周期性watermark,设置延迟时间为5秒
      //第一种写法，采用AssignerWithPeriodicWatermarks
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[game_gateway_data](Time.seconds(3)) {
      """抽取时间戳，设置event time"""

      override def extractTimestamp(element: game_gateway_data): Long = {
        element.gateway_time
      }
    })


    //第二种写法，自己定义实现类
    //      .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[game_gateway_data] {
    //      var maxEventTime: Long = _
    //
    //      //周期性生成watermark
    //      override def getCurrentWatermark = {
    //        new Watermark(maxEventTime - 3000L)
    //      }
    //
    //      //设定event time
    //      override def extractTimestamp(element: game_gateway_data, previousElementTimestamp: Long) = {
    //        //设置maxEventTime
    //        maxEventTime = maxEventTime.max(element.gateway_time)
    //        element.gateway_time
    //      }
    //    })

    gameGatewayStream
      .keyBy((_: game_gateway_data).user_id)
      .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
      .reduce(new MyReduceFunction, new stepTimeWindowFunction)
      .print("====>")

    env.execute("orderness case")

  }


  class MyReduceFunction extends ReduceFunction[game_gateway_data] {
    override def reduce(t: game_gateway_data, t1: game_gateway_data): game_gateway_data = {
      if (t.delay_time >= t1.delay_time) t else t1
    }
  }

  class stepTimeWindowFunction extends WindowFunction[game_gateway_data, game_gateway_data, String, TimeWindow] {
    // 在窗口结束时调用
    override def apply(key: String, window: TimeWindow, input: Iterable[game_gateway_data], out: Collector[game_gateway_data]): Unit = {
      // input中只有一条数据
      println(s"${window.getStart}===${window.getEnd}")
      val result: game_gateway_data = input.iterator.next()
      out.collect(result)
    }
  }


}


