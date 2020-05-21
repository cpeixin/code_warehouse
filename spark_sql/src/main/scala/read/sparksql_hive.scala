package read

import org.apache.spark.sql.{DataFrame, SparkSession}

object sparksql_hive {
  def main(args: Array[String]): Unit = {
    val warehouseLocation="hdfs://localhost:8020/user/hive/warehouse"
    val spark: SparkSession = SparkSession
      .builder()
      .appName("sql_case")
      .master("local")
      .config("spark.sql.warehouse.dir",warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    val df_databases: DataFrame = spark.sql("show databases")
    df_databases.show()
  }
}
