package streaming

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent



object streaming_updatastatebykey {

  // 定义数据格式
  case class keywordFormat(keyword: String, num: Int)

  Logger.getLogger("org").setLevel(Level.ERROR)

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("Kafka Streaming")
      .setMaster("local[2]")

    val ssc = new StreamingContext(conf, Seconds(2))
    ssc.checkpoint("/Users/cpeixin/IdeaProjects/code_warehouse/spark_streaming/src/main/scala/streaming/")

    val kafkaParams: Map[String, Object] = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "kafka_spark_streaming",
      "auto.offset.reset" -> "latest", // earliest
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("weibo_keyword")
    val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils
      .createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))

    val wordcount_dstream: DStream[(String, Int)] = kafkaStream
      .map((x: ConsumerRecord[String, String]) => {
        change_data(x.value())
      })
      .flatMap((_: String).split(","))
      .map((x: String) => (x, 1))

    val sum_dstream: DStream[(String, Int)] = wordcount_dstream.updateStateByKey((seq: Seq[Int], state: Option[Int]) => {
      var sum: Int = state.getOrElse(0)+seq.sum
      Option(sum)
    })

    sum_dstream.foreachRDD((keywordFormat_rdd: RDD[(String, Int)]) => {
      val sort_rdd: Array[(Int, String)] = keywordFormat_rdd.map((x: (String, Int)) => {(x._2, x._1)}).sortByKey().top(10)
      sort_rdd.foreach(println)
      println("====================")
    })

    ssc.start()
    ssc.awaitTermination()
  }

  def change_data(string_data: String): String = {
    val json_data: JSONObject = JSON.parseObject(string_data)
    val date_time: String = json_data.get("datetime").toString
    val keywordList: String = json_data.get("keywordList").toString
    keywordList
  }
}
