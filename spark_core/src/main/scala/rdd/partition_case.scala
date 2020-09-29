package rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object partition_case {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("function_case")
      .master("local[*]")
      .config("spark.sql.crossJoin.enabled", "true")
//      .config("spark.default.parallelism", 5)
      .getOrCreate()


    val rdd_case: RDD[Int] = spark.sparkContext.parallelize(Seq(1,2,3,4,5,5,5,5,6,7,8))
    println(rdd_case.partitions.length)// 默认为1， local[2]为2， 设置parallelism为 5

    val kv_rdd: RDD[(Int, Iterable[Int])] = rdd_case.map((x: Int) =>(x,1)).groupByKey()

    println(kv_rdd.partitions.length)

  }
}
