package unit_test

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object WordCount extends Serializable{

  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("test_rdd").setMaster("local")

    val sc = new SparkContext(sparkConf)

    val make_rdd: RDD[String] = sc.parallelize(Array("Brent","HayLee","Henry"))

    val result_rdd: RDD[(String, Int)] = count(make_rdd)

    result_rdd.foreach(println)
  }

  def count(rdd:RDD[String]): RDD[(String,Int)]={
    val wordcount_rdd: RDD[(String, Int)] =rdd.map((word: String) =>(word,1))
      .reduceByKey((_: Int) + (_: Int))
    wordcount_rdd
  }

}