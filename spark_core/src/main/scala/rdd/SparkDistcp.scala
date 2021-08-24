package rdd

import org.apache.spark.sql.SparkSession

/**
 * @author Congpeixin
 * @date 2021/8/24 6:31 下午
 * @version 1.0
 * @describe
 */
object SparkDistcp {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("SparkDistcp").master("local[2]").getOrCreate()
    HDFSFileUtil
  }
}
