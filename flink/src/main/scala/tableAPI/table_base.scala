//package tableAPI
//
//object table_base {
//  def main(args: Array[String]): Unit = {
//    // create a TableEnvironment for specific planner batch or streaming
//    val tableEnv = ... // see "Create a TableEnvironment" section
//
//    // create a Table
//    tableEnv.connect(...).createTemporaryTable("table1")
//    // register an output Table
//    tableEnv.connect(...).createTemporaryTable("outputTable")
//
//    // create a Table from a Table API query
//    val tapiResult = tableEnv.from("table1").select(...)
//    // create a Table from a SQL query
//    val sqlResult  = tableEnv.sqlQuery("SELECT ... FROM table1 ...")
//
//    // emit a Table API result Table to a TableSink, same for SQL result
//    tapiResult.insertInto("outputTable")
//
//    // execute
//    tableEnv.execute("scala_job")
//  }
//}
