package rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object make_rdd {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("test_rdd").setMaster("local")

    val sc = new SparkContext(sparkConf)

    val make_rdd: RDD[Int] = sc.makeRDD(List(1,2,3,4))
    val make_rdd_1: RDD[String] = sc.parallelize(Array("Brent","HayLee","Henry"))
    val make_rdd_2: RDD[Int] = sc.makeRDD(List(1,2,3,4),2)
    val make_rdd_3: RDD[String] = sc.parallelize(Array("Brent","HayLee","Henry"),4)

    println(make_rdd.partitions.size)
    println(make_rdd_1.partitions.size)
    println(make_rdd_2.partitions.size)
    println(make_rdd_3.partitions.size)

}
}
