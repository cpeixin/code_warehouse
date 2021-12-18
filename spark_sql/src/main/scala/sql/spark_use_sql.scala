package sql

import org.apache.spark.sql.{DataFrame, SparkSession}

object spark_use_sql {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("sql_case")
      .master("local")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate()

    // 样例数据
    /**
      * {"user_name":"brent","customer_id":12031602,"age": 22,"birthday":"1993-04-05","deposit_amount":3000,"last_login_time":"2017-03-10 14:55:22"}
      * {"user_name":"haylee","customer_id":12031603,"age":23,"birthday":"1992-08-10","deposit_amount":4000.56,"last_login_time":"2017-03-11 10:55:00"}
      * {"user_name":"vicky","customer_id":12031604,"age":30,"birthday":"2000-03-02","deposit_amount":200.4,"last_login_time":"2017-03-10 09:10:00"}
      */
    val df: DataFrame = spark.read.json("data/user_data.json")

    df.createTempView("t_user")

    val user_df = spark.sql("select * from t_user")
//    +---+----------+-----------+--------------+-------------------+---------+
//    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
//    +---+----------+-----------+--------------+-------------------+---------+
//    | 22|1993-04-05|   12031602|        3000.0|2017-03-10 14:55:22|    brent|
//    | 23|1992-08-10|   12031603|       4000.56|2017-03-11 10:55:00|   haylee|
//    | 30|2000-03-02|   12031604|         200.4|2017-03-10 09:10:00|    vicky|
//    +---+----------+-----------+--------------+-------------------+---------+

    user_df.foreachPartition(record=>{
      record.foreach(x=>{
        println(x.getAs[String]("age"))
      })
    })

    import spark.implicits._
    val res_df = user_df.mapPartitions(x=>{
      x.map(ele=>{
        println(ele.getAs[String]("age"))
        ele.getAs[Long]("age")
      })
    })
    res_df.show()


    }
  }
