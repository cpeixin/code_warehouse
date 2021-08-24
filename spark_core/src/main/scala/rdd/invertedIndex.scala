package rdd

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapred.{FileSplit, InputSplit, TextInputFormat}
import org.apache.log4j.Logger
import org.apache.spark.rdd.{NewHadoopRDD, RDD}
import org.apache.spark.sql.SparkSession

/**
 * @author Congpeixin
 * @date 2021/8/21 10:54 下午
 * @version 1.0
 * @describe
 */
object invertedIndex {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("invertedIndex").master("local[2]").getOrCreate()

    val filePath = "/Users/dongqiudi/IdeaProjects/code_warehouse/spark_core/src/main/scala/rdd/data/"
    // 读取多个文件，并附有文件名
    val originalRDD: RDD[(String, String)] = spark.sparkContext.wholeTextFiles(filePath)

    // 1. 文件内容单词切分、压扁
    // 2. 单词与文件名对应
    // eg. (it,0)
    //     (it,2)
    //     (is,0)
    //     (is,2)
    //     (what,0)
    //     (a,2)
    //     (banana,2)
    //     (it,0)
    val wordInFileNameRDD = originalRDD.flatMap(line => {
      val fileName = line._1
      val wordArray = line._2.replace("\"", "").split(" ")
      wordArray.map((_, fileName.replace("file:" + filePath, "")))
    })
    wordInFileNameRDD.foreach(println)

    // groupByKey分组，将单词对应的文件名放到一个组中
    // 结果一：
    //     反向文件索引：
    //     "a": {2}
    //     "banana": {2}
    val res1RDD = wordInFileNameRDD.groupByKey().map(kv => (kv._1, kv._2.toSet))
    res1RDD.foreach(println)

    // (it,0) -> mapValues (it,(0,1)) , (it,(0,1)) -> groupByKey -> (it,CompactBuffer((0,1), (0,1), (1,1), (2,1)))
    // (it,CompactBuffer((0,1), (0,1), (1,1), (2,1))) -> map(groupBy(CompactBuffer.element(_.1))) -> reduce 聚合
    // 结果二：
    //  "a": {(2,1)}
    //  "banana": {(2,1)}
    //  "is": {(0,2), (1,1), (2,1)}
    //  "it": {(0,2), (1,1), (2,1)}
    val res2RDD = wordInFileNameRDD.mapValues((_, 1)).groupByKey().map(kv => {
      val stringToTuples = kv._2.toSeq.groupBy(line => line._1).map(line => {
        val fileName = line._1
        val count = line._2.reduce((a, b) => (a._1, (a._2 + b._2)))._2
        (fileName, count)
      })
      (kv._1, stringToTuples)
    })
    res2RDD.foreach(println)
  }

}
