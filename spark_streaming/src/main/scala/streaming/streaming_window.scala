package streaming

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object streaming_window {
  Logger.getLogger("org").setLevel(Level.ERROR)

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      .setAppName("spark streaming window")
      .setMaster("local[2]")

    val sc = new StreamingContext(conf, Seconds(5))

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
      .createDirectStream[String, String](sc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))

    val original_dstream: DStream[String] = kafkaStream
      .map((x: ConsumerRecord[String, String]) => {
        get_data(x.value())
      })

    val wordcount_dstream: DStream[(String, Int)] = set_word_num(original_dstream)


    val window_dstream: DStream[(String, Int)] = wordcount_dstream.reduceByKeyAndWindow((x: Int,y: Int)=>x+y,Seconds(30), Seconds(10))

    val result: DStream[(String, Int)] = window_dstream.transform((rdd: RDD[(String, Int)]) =>{
      val top5: Array[(String, Int)] = rdd.map((x: (String, Int)) =>(x._2,x._1)).sortByKey(ascending = false).map((x: (Int, String)) =>(x._2,x._1)).take(5)
      sc.sparkContext.makeRDD(top5)
    })

    result.print()

    sc.start()
    sc.awaitTermination()

  }

  def set_word_num(dstream: DStream[String]): DStream[(String, Int)] = {
    val value: DStream[(String, Int)] = dstream.flatMap((_: String).split(",")).map((x: String) => (x, 1))
    value
  }

  def get_data(string_data: String): String = {
    val json_data: JSONObject = JSON.parseObject(string_data)
    val date_time: String = json_data.get("datetime").toString
    val keywordList: String = json_data.get("keywordList").toString
    keywordList
  }
}
