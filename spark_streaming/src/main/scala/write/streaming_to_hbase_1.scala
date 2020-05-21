package write

import java.security.MessageDigest

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Connection, Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.elasticsearch.spark.streaming.EsSparkStreaming

import utils.HBaseUtil

import scala.util.Try

object streaming_to_hbase_1 {
  val logger:Logger = Logger.getRootLogger
  Logger.getLogger("org").setLevel(Level.ERROR)
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf()
      .setAppName("spark streaming window")
      .setMaster("local[2]")
      .set("spark.es.nodes", "localhost")
      .set("spark.es.port", "9200")
      .set("es.index.auto.create", "true")


    val ssc = new StreamingContext(conf, Seconds(5))

    val kafkaParams: Map[String, Object] = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "kafka_spark_streaming",
      "auto.offset.reset" -> "earliest", // earliest，latest
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    val topics = Array("weibo_keyword")

    val kafkaStream: DStream[(String, String)] = KafkaUtils
      .createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))
      .map((x: ConsumerRecord[String, String]) => {
        val json_data: JSONObject = JSON.parseObject(x.value())
        val date_time: String = json_data.get("datetime").toString
        val keywordList: String = json_data.get("keywordList").toString
        (date_time, keywordList)
      })

    // ES 数据写入部分
    val es_dstream: DStream[String] = kafkaStream.map((x: (String, String)) => {
      val date_time: String = x._1.replace("2020-05-05", "2017-04-22").replace("2020-05-04", "2017-04-22")
      val keywordList: String = x._2
      val data_es_json: JSONObject = new JSONObject()
      data_es_json.put("date_time", date_time)
      data_es_json.put("keyword_list", keywordList)
      data_es_json.put("rowkey", MD5Encode(date_time))
      data_es_json.toJSONString
    })

    EsSparkStreaming.saveJsonToEs(es_dstream,"weibo_keyword-2017-04-25/default")

    //HBase 数据写入部分
    kafkaStream.foreachRDD((rdd: RDD[(String, String)]) => {
      val es_rdd: RDD[JSONObject] = rdd.map((x: (String, String)) => {
        val date_time: String = x._1.replace("2020-05-05", "2017-04-22").replace("2020-05-04", "2017-04-22")
        val keywordList: String = x._2

        val data_es_json: JSONObject = new JSONObject()
        data_es_json.put("date_time", date_time)
        data_es_json.put("keyword_list", keywordList)
        data_es_json.put("rowkey", MD5Encode(date_time))

        data_es_json
      })

      rdd.foreachPartition((partitionRecords: Iterator[(String, String)]) => {//循环分区
        try {
          val connection: Connection = HBaseUtil.getHBaseConn //获取HBase连接,分区创建一个连接，分区不跨节点，不需要序列化
          partitionRecords.foreach((s: (String, String)) => {

            val tableName: TableName = TableName.valueOf("t_weibo_keyword_2")
            val table: Table = connection.getTable(tableName)//获取表连接

            var date_time: String = s._1.replace("2020-05-05","2017-04-22").replace("2020-05-04","2017-04-22")
            val keywordList: String = s._2
            val put = new Put(Bytes.toBytes(MD5Encode(date_time)))
            put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("keywordList"), Bytes.toBytes(keywordList))

            Try(table.put(put)).getOrElse(table.close())//将数据写入HBase，若出错关闭table
//            table.close()//分区数据写入HBase后关闭连接

          })
        } catch {
          case e: Exception =>
            logger.info(e)
            logger.info("写入HBase失败")
        }
      })

    })

    ssc.start()
    ssc.awaitTermination()
  }

  def MD5Encode(input: String): String = {
    // 指定MD5加密算法
    val md5: MessageDigest = MessageDigest.getInstance("MD5")
    // 对输入数据进行加密,过程是先将字符串中转换成byte数组,然后进行随机哈希
    val encoded: Array[Byte] = md5.digest(input.getBytes)
    // 将加密后的每个字节转化成十六进制，一个字节8位，相当于2个16进制，不足2位的前面补0
    encoded.map("%02x".format(_: Byte)).mkString
  }

}
