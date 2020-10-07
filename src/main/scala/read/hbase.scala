//package read
//
//import org.apache.spark.sql.{DataFrame, SparkSession}
//import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
//
//import scala.collection.immutable
//
//object sparksql_hbase {
//
//  def main(args: Array[String]): Unit = {
//    case class Record(col0: Int, col1: Int, col2: Boolean)
//
//    val spark: SparkSession = SparkSession
//      .builder()
//      .appName("Spark HBase Example")
//      .master("local[4]")
//      .getOrCreate()
//
//    def catalog: String =
//    // 这里，我们在读取数据的过程中，无论什么类型的数据，type字段统一指定成 string 即可。否则读取报错
//      s"""{
//         |"table":{"namespace":"default", "name":"t_user"},
//         |"rowkey":"key",
//         |"columns":{
//         |"col0":{"cf":"rowkey", "col":"key", "type":"string"},
//         |"col1":{"cf":"cf1", "col":"user_name", "type":"string"},
//         |"col2":{"cf":"cf1", "col":"customer_id", "type":"string"},
//         |"col3":{"cf":"cf1", "col":"age", "type":"string"},
//         |"col4":{"cf":"cf1", "col":"birthday", "type":"string"},
//         |"col5":{"cf":"cf1", "col":"deposit_amount", "type":"string"},
//         |"col6":{"cf":"cf1", "col":"last_login_time", "type":"string"},
//         |"col7":{"cf":"cf1", "col":"flag", "type":"string"}
//         |}
//         |}""".stripMargin
//
//
//    // read
//    val df: DataFrame = spark
//      .read
//      .option(HBaseTableCatalog.tableCatalog, catalog)
//      .format("org.apache.spark.sql.execution.datasources.hbase")
//      .load()
//
//    df.show()
//
//  }
//}