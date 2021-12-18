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

    val originalRDD: RDD[String] = spark.sparkContext.textFile("/Users/dongqiudi/IdeaProjects/code_warehouse/spark_core/src/main/scala/rdd/0300-172.21.1.128-a172.21.1.128_1.1630436400838.part")

    val kvRDD: RDD[(String, Int)] = originalRDD.map((_, 1)).repartition(10)

//    import spark.implicits._
//    val ds = Seq(("a", 10), ("a", 20), ("b", 1), ("b", 2), ("c", 1)).toDS()
//    val grouped = ds.groupByKey(v => (v._1, "word"))
//    val aggregated = grouped.flatMapGroups { (g, iter) =>
//      Iterator(g._1, iter.)
//    }

//    aggregated.show()

//    val partition_0: Partition = kvRDD.partitions(0)
//    val dependencies: Dependency[_] = kvRDD.dependencies.head
//    val preferredLocation: Seq[String] = kvRDD.preferredLocations(kvRDD.partitions(0))
//    println(partition_0)
//    println(dependencies)
//    println(preferredLocation)
//    println(originalRDD.toDebugString)
//    kvRDD.toDebugString
//    kvRDD.take(1)

    spark.close()
  }
}
