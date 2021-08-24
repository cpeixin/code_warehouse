package rdd

import org.apache.spark.{Dependency, Partition}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.collection.mutable.ListBuffer
import scala.{+:, ::}

/**
 * @author Congpeixin
 * @date 2021/8/10 8:35 上午
 * @version 1.0
 * @describe
 */
object RDDPartition {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("RDDPartition").master("local[2]").getOrCreate()

    val originalRDD: RDD[String] = spark.sparkContext.textFile("/Users/dongqiudi/IdeaProjects/code_warehouse/spark_core/src/main/scala/rdd/1625-172.21.1.70-a172.21.1.70_1.1614241500861")

    val kvRDD: RDD[(String, Int)] = originalRDD.map((_, 1))

    val partition_0: Partition = kvRDD.partitions(0)
    val dependencies: Dependency[_] = kvRDD.dependencies.head
    val preferredLocation: Seq[String] = kvRDD.preferredLocations(kvRDD.partitions(0))

    println(partition_0)
    println(dependencies)
    println(preferredLocation)
    println(originalRDD.toDebugString)
    println(kvRDD.toDebugString)
    kvRDD.take(1)

    spark.close()
  }
}
