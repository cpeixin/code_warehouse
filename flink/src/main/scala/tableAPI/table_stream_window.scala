//package tableAPI
//
//import org.apache.flink.streaming.api.TimeCharacteristic
//import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
//import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
//import org.apache.flink.streaming.api.windowing.time.Time
//import org.apache.flink.table.api.{EnvironmentSettings, Slide, Table, Tumble}
//import org.apache.flink.table.api._
//import org.apache.flink.types.Row
//import org.apache.flink.streaming.api.scala._
//import org.apache.flink.table.api.scala.StreamTableEnvironment
//
//
//object table_stream_window {
//
//  case class GameData(user_id: String, game_id: String, game_time: Long, game_score: Int)
//
//  def main(args: Array[String]): Unit = {
//    val stream_env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//    stream_env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
//    val settings: EnvironmentSettings = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build()
//    val table_env: StreamTableEnvironment = StreamTableEnvironment.create(stream_env, settings)
//    stream_env.setParallelism(1)
//    val socketStream: DataStream[String] = stream_env.socketTextStream("localhost", 8888)
//
//    val gameStream: DataStream[GameData] = socketStream.map((line: String) => {
//      val array_data: Array[String] = line.split(",")
//      GameData(array_data(0), array_data(1), array_data(2).toLong, array_data(3).toInt)
//    }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[GameData](Time.seconds(3)) {
//      override def extractTimestamp(element: GameData): Long = {
//        element.game_time
//      }
//    })
//
//    //分组聚合操作
//
//
//    //创建动态Table,并且指定event time
//    val game_table: Table = table_env.fromDataStream(gameStream, 'user_id, 'game_id, 'game_time.rowtime, 'game_score)
//
//
//    //开启窗口,滑动窗口
//    //    game_table.window(Slide.over("10.second").every("5.second").on("game_time").as("window"))
//
//    //开启窗口,滚动窗口
//    //    val game_score_sum: Table = game_table.window(Tumble.over("5.second").on("game_time").as("window"))
//    val game_score_sum: Table = game_table
//      .window(Tumble over 5.second on 'game_time as 'window)
//      .groupBy('window, 'user_id) //必须指定窗口字段
//      .select('user_id, 'window.start, 'window.end, 'game_score.sum)
//
//    table_env
//      .toRetractStream[Row](game_score_sum)
//      .filter((_: (Boolean, Row))._1 == true)
//      .print("user_sum_score")
//
//    table_env.execute("user_sum_score——job")
//  }
//}
