package SQL


import org.apache.flink.api.scala.typeutils.Types
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.{DataTypes, EnvironmentSettings, Table}
import org.apache.flink.table.api._
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.table.sources.CsvTableSource
import org.apache.flink.types.Row

object sql_table_source {
  def main(args: Array[String]): Unit = {
    val stream_env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val settings: EnvironmentSettings = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build()
    val table_env: StreamTableEnvironment = StreamTableEnvironment.create(stream_env, settings)

    val table_source = new CsvTableSource("/Users/cpeixin/IdeaProjects/code_warehouse/data/game_data.csv"
      , Array[String]("user_id", "game_id", "game_time", "game_score")
      , Array(Types.STRING, Types.STRING, Types.LONG, Types.INT))

    table_env.registerTableSource("t_game_detail", table_source)

    val result: Table = table_env.sqlQuery("select user_id, sum(game_score) as game_score_sum from t_game_detail group by user_id")


//    table_env.toRetractStream[Row](result)
//      .filter((_: (Boolean, Row))._1== true)
//      .print()

    table_env.execute("sql case")
  }
}
