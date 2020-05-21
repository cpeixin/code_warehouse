//package write
//
//import java.io.IOException
//import java.util
//
//import com.alibaba.fastjson.{JSON, JSONObject}
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
//import org.apache.hadoop.hbase.client._
//import org.apache.hadoop.hbase.util.Bytes
//import org.apache.kafka.clients.consumer.ConsumerRecord
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.apache.spark.SparkConf
//import org.apache.spark.rdd.RDD
//import org.apache.spark.streaming.dstream.{DStream, InputDStream}
//import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
//import org.apache.spark.streaming.kafka010.KafkaUtils
//import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//
//object streaming_to_hbase {
//  def main(args: Array[String]) {
//
//    val conf: SparkConf = new SparkConf()
//      .setAppName("spark streaming window")
//      .setMaster("local[2]")
//
//    val ssc = new StreamingContext(conf, Seconds(5))
//
//    val columnFamily = "cf"
//
//    val kafkaParams: Map[String, Object] = Map[String, Object](
//      "bootstrap.servers" -> "localhost:9092",
//      "key.deserializer" -> classOf[StringDeserializer],
//      "value.deserializer" -> classOf[StringDeserializer],
//      "group.id" -> "kafka_spark_streaming",
//      "auto.offset.reset" -> "latest", // earliest
//      "enable.auto.commit" -> (false: java.lang.Boolean)
//    )
//
//    val topics = Array("weibo_keyword")
//
//    val kafkaStream: DStream[(String, String)] = KafkaUtils
//      .createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topics, kafkaParams))
//      .map((x: ConsumerRecord[String, String]) => {
//        val json_data: JSONObject = JSON.parseObject(x.value())
//        val date_time: String = json_data.get("datetime").toString
//        val keywordList: String = json_data.get("keywordList").toString
//        (date_time, keywordList)
//      })
//
//
//    kafkaStream.foreachRDD((rdd: RDD[(String, String)]) => {
//
//      //Partition runs on the executor.
//      rdd.foreachPartition((iterator: Iterator[(String, String)]) => hBaseWriter(iterator, columnFamily))
//
//    })
//
//    ssc.start()
//
//    ssc.awaitTermination()
//
//  }
//
//  /**
//
//    * Write data to the executor.
//
//    * @param iterator message
//
//    * @param columnFamily
//
//    */
//
//  def hBaseWriter(iterator: Iterator[(String, String)], columnFamily: String): Unit = {
//
//    val conf: Configuration = HBaseConfiguration.create()
//
//    var table: Table = null
//
//    var connection: Connection = null
//
//    try {
//
//      connection = ConnectionFactory.createConnection(conf)
//
//      table = connection.getTable(TableName.valueOf("table1"))
//
//      val iteratorArray: Array[(String, String)] = iterator.toArray
//
//      val rowList = new util.ArrayList[Get]()
//
//      for (row <- iteratorArray) {
//
//        val get = new Get(row.toBytes)
//
//        rowList.add(get)
//
//      }
//
//      // Obtain table1 data.
//
//      val resultDataBuffer = table.get(rowList)
//
//      // Set the table1 data.
//
//      val putList = new util.ArrayList[Put]()
//
//      for (i <- 0 until iteratorArray.size) {
//
//        val row = iteratorArray(i)
//
//        val resultData = resultDataBuffer(i)
//
//        if (!resultData.isEmpty) {
//
//          // Obtain the old value based on the column family and column.//
//
//          val aCid = Bytes.toString(resultData.getValue(columnFamily.getBytes, "cid".getBytes))
//
//          val put = new Put(Bytes.toBytes(row))
//
//          // Calculation result //
//
//          val resultValue = row.toInt + aCid.toInt
//
//          put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes("cid"), Bytes.toBytes(resultValue.toString))
//
//          putList.add(put)
//
//        }
//
//      }
//
//      if (putList.size() > 0) {
//
//        table.put(putList)
//
//      }
//
//    } catch {
//
//      case e: IOException =>
//
//        e.printStackTrace();
//
//    } finally {
//
//      if (table != null) {
//
//        try {
//
//          table.close()
//
//        } catch {
//
//          case e: IOException =>
//
//            e.printStackTrace();
//
//        }
//
//      }
//
//      if (connection != null) {
//
//        try {
//
//          // Close the HBase connection.//
//
//          connection.close()
//
//        } catch {
//
//          case e: IOException =>
//
//            e.printStackTrace()
//
//        }
//
//      }
//
//    }
//
//  }
//
//  private def printUsage {
//
//    System.out.println("Usage: {checkPointDir} {topic} {brokerList}")
//
//    System.exit(1)
//
//  }
//}
