package rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object process_corpus {
  def main(args: Array[String]): Unit={
    val sparkConf: SparkConf = new SparkConf()
      .setAppName("process_json")
        .setMaster("local[3]")

    val sc = new SparkContext(sparkConf)
    var json_rdd: RDD[String] = sc.textFile("/Users/cpeixin/train.json")

    val result: Array[String] = json_rdd.take(3)
    
    result.foreach(println)

    sc.stop()

  }
}
