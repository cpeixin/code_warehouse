package SparkListenerBus

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author Congpeixin
 * @date 2021/11/25 9:23 上午
 * @version 1.0
 * @describe
 */
object JobProcesser {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("KafkaWordCountProducer").setMaster("local")
    val sc = new SparkContext(sparkConf)
    /*  sc.setJobGroup("test1","testdesc")
      val completedJobs= sc.jobProgressListener*/
    sc.addSparkListener(new MySparkListener)
    val rdd1 = sc.parallelize(List(('a', 'c', 1), ('b', 'a', 1), ('b', 'd', 8)))
    val rdd2 = sc.parallelize(List(('a', 'c', 2), ('b', 'c', 5), ('b', 'd', 6)))
    val rdd3 = rdd1.union(rdd2)
    rdd3.foreach(println)
    sc.stop()
  }
}
