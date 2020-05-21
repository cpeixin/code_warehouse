package hdfs

import org.apache.spark.sql.SparkSession


object hdfsCase {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("hdfs")
      .master("local[2]")
      .getOrCreate()

    
  }
}
