package streaming

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import org.apache.log4j.{Level, Logger}


object streaming_weibo {
  // 设置日志级别
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
      "auto.offset.reset" -> "earliest", // earliest
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("weibo_keyword")
    val kafkaStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils
      .createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))

    kafkaStream.map((x: ConsumerRecord[String, String]) => {
      change_data(x.value())
    }).foreachRDD((x: RDD[String]) => x.foreach(println))

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
