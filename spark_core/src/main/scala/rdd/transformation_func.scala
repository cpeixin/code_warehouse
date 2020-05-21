package rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object transformation_func {
  def main(args: Array[String]): Unit ={
    val sparkConf: SparkConf = new SparkConf()
      .setAppName("transformation_func")
      .setMaster("local")

    val sc = new SparkContext(sparkConf)

    var original_rdd: RDD[(String, Int)] = sc.parallelize(Array(("a", 1), ("b", 1), ("a", 2),("c",4),("c",4)),2)


    var map_rdd: RDD[(String, Int)] = original_rdd.map(x =>(x._1,x._2+1))

    println("map操作：对original_rdd每个数据的第二个元素+1")
    map_rdd.foreach(println)

    println("filter操作：过滤掉original_rdd中，第一个元素不为a的数据")
    var filter_rdd: RDD[(String, Int)] = original_rdd.filter(x => x._1 == "a")
    filter_rdd.foreach(println)

    println("flatmap操作：对original_rdd做映射扁平化操作")
    val flatmap_rdd: RDD[Char] = original_rdd.flatMap(x=> x._1 + x._2)
    flatmap_rdd.foreach(println)

    println("mapPartitions操作：对original_rdd每个分区做相应操作")
    val mapPartitions_rdd: RDD[(String, Int)] = original_rdd.mapPartitions(x=>{x.map(item=>(item._1,item._2+1))})
    mapPartitions_rdd.foreach(println)


    println("sample操作：提取样本")
    val sample_rdd: RDD[(String, Int)] = original_rdd.sample(true, 0.25)
    sample_rdd.foreach(println)


    println("distinct操作：去重")
    val distinct_rdd: RDD[(String, Int)] = original_rdd.distinct()
    distinct_rdd.foreach(println)


    println("groupbykey操作：分组聚合")
    val groupByKey_rdd: RDD[(String, Iterable[Int])] = original_rdd.groupByKey()
    groupByKey_rdd.foreach(println)


    println("reduceByKey操作：聚合")
    val reduceByKey_rdd: RDD[(String, Int)] = original_rdd.reduceByKey(_+_)
    reduceByKey_rdd.foreach(println)

    println("sortByKey操作：排序")
    val sortByKey_rdd: RDD[(String, Int)] = original_rdd.sortByKey()
    sortByKey_rdd.foreach(println)





  }

}
