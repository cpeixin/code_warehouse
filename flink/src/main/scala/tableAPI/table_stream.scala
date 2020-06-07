package tableAPI

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{EnvironmentSettings, Table}
import org.apache.flink.table.api.scala.StreamTableEnvironment
import org.apache.flink.streaming.api.scala._
import org.apache.flink.types.Row

object table_stream {

  case class GameData(user_id: String, game_id: String, game_time: Long, game_score: Int)

  def main(args: Array[String]): Unit = {
    val stream_env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val settings: EnvironmentSettings = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build()
    val table_env: StreamTableEnvironment = StreamTableEnvironment.create(stream_env, settings)
//    stream_env.setParallelism(1)
    val socketStream: DataStream[String] = stream_env.socketTextStream("localhost", 8888)

    val gameStream: DataStream[GameData] = socketStream.map((line: String) => {
      val array_data: Array[String] = line.split(",")
      GameData(array_data(0), array_data(1), array_data(2).toLong, array_data(3).toInt)
    })



    //  打印表结构
    //    table_env.registerDataStream("t_game_detail", gameStream)
    //    val t_game_detail: Table = table_env.scan("t_game_detail")
    //    t_game_detail.printSchema()


    //查询,去重操作
    //这里需注意，使用去重操作时，不能使用toAppendStream。
    //val t_game_detail: Table = table_env.fromDataStream(gameStream).select("user_id").distinct()

    //过滤操作
    //val t_game_detail: Table = table_env.fromDataStream(gameStream).filter("game_score>500")

    //分组聚合操作
    import org.apache.flink.table.api.scala._
    val t_game_detail: Table = table_env.fromDataStream(gameStream).groupBy('user_id).select('user_id, 'user_id.count as 'count)//.select('user_id, 'game_score.sum as 'sum_game_score)
      //.groupBy('user_id).select('user_id, 'game_score.avg as 'avg_game_score)


    //  这里如果对原始数据没有做字段的添加，修改，删除等，toAppendStream的类型可以为case class类型
    //  如果对字段进行修改了，可以设置为Row类型，会简化操作
    val result: DataStream[(Boolean, Row)] = table_env.toRetractStream[Row](t_game_detail).filter((_: (Boolean, Row))._1 == true)


    result.print("t_game_detail")

    stream_env.execute("stream table")


  }

}
