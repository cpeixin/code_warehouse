package sql

import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{DataFrame, SparkSession}

case class demo(user_name: String, age: Long)

object catalyst_case {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("function_case")
      .master("local")
      .config("spark.sql.crossJoin.enabled", "true")
      .getOrCreate()

    // 样例数据
    /**
      * {"user_name":"brent","customer_id":12031602,"age": 22,"birthday":"1993-04-05","deposit_amount":3000,"last_login_time":"2017-03-10 14:55:22"}
      * {"user_name":"haylee","customer_id":12031603,"age":23,"birthday":"1992-08-10","deposit_amount":4000.56,"last_login_time":"2017-03-11 10:55:00"}
      * {"user_name":"vicky","customer_id":12031604,"age":30,"birthday":"2000-03-02","deposit_amount":200.4,"last_login_time":"2017-03-10 09:10:00"}
      */
    val df: DataFrame = spark.read.json("/Users/dongqiudi/IdeaProjects/code_warehouse/data/user_data.json")
    import spark.implicits._
    val res = df.map(record =>
      demo(record.getAs[String]("user_name"), record.getAs[Long]("age"))
    ).toDF("user_name","age")

    res.map(record =>record.getAs[String]("user_name")).show
  }
}
