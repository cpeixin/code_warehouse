package data_stream.watermark


import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.function.{ProcessWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

object watermark_allowedLateness {

  case class GameData(user_id: String, game_id: String, game_time: Long, game_score: Int)


  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.getConfig.setAutoWatermarkInterval(100L) //watermark周期

    val socketStream: DataStream[String] = env.socketTextStream("localhost", 8888)
    val gameStream: DataStream[GameData] = socketStream.map((line: String) => {
      val array_data: Array[String] = line.split(",")
      GameData(array_data(0), array_data(1), array_data(2).toLong, array_data(3).toInt)
    })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[GameData](Time.seconds(2)) {
        override def extractTimestamp(element: GameData) = {
          element.game_time
        }
      })


    var gameLateData = new OutputTag[GameData]("late")


    val windowStream: DataStream[(String, List[Int])] = gameStream
      .keyBy((_: GameData).user_id)
      .timeWindow(Time.seconds(10), Time.seconds(5))
      //数据延迟超过2秒，交给allowedLateness来处理
      .allowedLateness(Time.seconds(20))
      .sideOutputLateData(gameLateData)
      //  接下里会发生三种情况：
      //      1.没有延迟或者延迟小于2秒的数据，watermark保证窗口的触发，正常进入窗口中计算
      //      2.数据延迟在2秒-20秒之间的数据，watermark+allowedLateness机制，watermark < window_end_time + allowedLateness_time时触发窗口
      //      3.数据延迟大于20秒的数据，则输入到侧输出流处理sideOutputLateData

      //第一种方法
      //      .process(new ProcessWindowFunction[GameData, (String, List[Int]), String, TimeWindow] {
      //        override def process(key: String, context: Context, elements: Iterable[GameData], out: Collector[(String, List[Int])]): Unit = {
      //          val scoreList: ListBuffer[Int] = ListBuffer[Int]()
      //          val scoreiterator: Iterator[GameData] = elements.iterator
      //          while (scoreiterator.hasNext) {
      //            val data: GameData = scoreiterator.next()
      //            scoreList += data.game_score
      //          }
      //          out.collect((key, scoreList.toList))
      //        }
      //      })

      //第二种方法
      .apply(new WindowFunction[GameData, (String, List[Int]), String, TimeWindow] {
      override def apply(key: String, window: TimeWindow, input: Iterable[GameData], out: Collector[(String, List[Int])]): Unit = {
        val scoreList: ListBuffer[Int] = ListBuffer[Int]()
        val scoreiterator: Iterator[GameData] = input.iterator
        while (scoreiterator.hasNext) {
          val data: GameData = scoreiterator.next()
          scoreList += data.game_score
        }
        println(s"${window.getStart}=====${window.getEnd}")
        out.collect((key, scoreList.toList))
      }
    })

    windowStream.print("window data")

    val late: DataStream[GameData] = windowStream.getSideOutput(gameLateData)
    late.print("迟到的数据:")

    env.execute(this.getClass.getName)

  }
}
