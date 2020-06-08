package SQL

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.table.api.{EnvironmentSettings, Table, Tumble}
import org.apache.flink.types.Row


object sql_stream_window {

  case class GameData(user_id: String, game_id: String, game_time: Long, game_score: Int)

  def main(args: Array[String]): Unit = {
    val stream_env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    stream_env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val settings: EnvironmentSettings = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build()
    val table_env: StreamTableEnvironment = StreamTableEnvironment.create(stream_env, settings)
    stream_env.setParallelism(1)
    val socketStream: DataStream[String] = stream_env.socketTextStream("localhost", 8888)

    val gameStream: DataStream[GameData] = socketStream.map((line: String) => {
      val array_data: Array[String] = line.split(",")
      GameData(array_data(0), array_data(1), array_data(2).toLong, array_data(3).toInt)
    }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[GameData](Time.seconds(3)) {
      override def extractTimestamp(element: GameData): Long = {
        element.game_time
      }
    })

    import org.apache.flink.table.api.scala._

    table_env.registerDataStream("t_game_detail", gameStream, 'user_id, 'game_id, 'game_time.rowtime, 'game_score)
    //滚动窗口
    //    val game_score_sum: Table = table_env
    //      .sqlQuery("select user_id, " +
    //        "sum(game_score) " +
    //        "from t_game_detail " +
    //        "group by tumble(game_time, interval '5' second), user_id")

    //    滑动窗口
    //    val game_score_sum: Table = table_env.sqlQuery("select user_id, " +
    //      "sum(game_score) " +
    //      "from t_game_detail " +
    //      "group by hop(game_time, interval '5' second, interval '10' second), user_id")

    //  滑动窗口，打印窗口时间
    val game_score_sum: Table = table_env.sqlQuery("select user_id, " +
      "hop_start(game_time, interval '5' second, interval '10' second)," +
      "hop_end(game_time, interval '5' second, interval '10' second)," +
      "sum(game_score) " +
      "from t_game_detail " +
      "group by hop(game_time, interval '5' second, interval '10' second), user_id")

    table_env
      .toRetractStream[Row](game_score_sum)
      .filter((_: (Boolean, Row))._1 == true)
      .print("user_sum_score")

    table_env.execute("user_sum_score——job")
  }
}
