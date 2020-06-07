//package tableAPI
//
//import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
//import org.apache.flink.table.api.scala.StreamTableEnvironment
//import org.apache.flink.table.api.{DataTypes, EnvironmentSettings, TableEnvironment}
//import org.apache.flink.table.descriptors.{FileSystem, Schema}
//
//
//object tableApi_file {
//  def main(args: Array[String]): Unit = {
//
////  第一步： 选择运行模式，这里选择流模式
//    val fsSettings: EnvironmentSettings = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build()
//    val fsEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//    val fsTableEnv: TableEnvironment = TableEnvironment.create(fsSettings)
//
//
////  第二步，读取数据源
//
//    val schema = new Schema()
//      .field("a", DataTypes.INT())
//      .field("b", DataTypes.STRING())
//      .field("c", DataTypes.LONG())
//
//    fsTableEnv.connect(new FileSystem())
//      .withFormat(new Csv().fieldDelimiter('|').deriveSchema())
//      .withSchema(schema)
//      .createTemporaryTable("CsvSinkTable")
//
//    // compute a result Table using Table API operators and/or SQL queries
//    val result: Table = ...
//
//    // emit the result Table to the registered TableSink
//    result.insertInto("CsvSinkTable")
//  }
//}
