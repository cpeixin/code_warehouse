package read

import org.apache.spark.sql.{DataFrame, SparkSession}

object sparksql_json {
  def main(args: Array[String]): Unit ={
    val spark: SparkSession = SparkSession
      .builder()
      .appName("sql_case")
      .master("local")
      .getOrCreate()

    val df_json: DataFrame = spark.read.json("hdfs://localhost:8020/data/user_data.json")

    df_json.show(5)

  }
}
