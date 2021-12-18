package rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

object count_avg {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession.builder().appName("ddd").master("local[2]").getOrCreate()
    val user_rdd: RDD[(String, Int, String)] = spark.sparkContext.parallelize(Seq(("test1", 23, "11"),("test1", 23, "12"),("test1", 23, "13"),("test2", 27, "11"),
      ("test2", 27, "11"),("test3", 29, "11"),("test3", 29, "12"),("test3", 29, "12"),("test3", 29, "12")))


    val user_distinct: RDD[(String, Int)] = user_rdd.map((x: (String, Int, String)) =>(x._1, x._2)).distinct()
    import spark.implicits._
    val user_df: DataFrame = user_distinct.toDF("user", "age")

    val frame: DataFrame = user_df.agg("user"->"count", "age"->"avg")

    frame.show
  }
}
