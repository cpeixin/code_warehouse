import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.log4j.{Level, Logger}

object structured_kafka {
  val logger:Logger = Logger.getRootLogger
  Logger.getLogger("org").setLevel(Level.ERROR)
  def main(args: Array[String]): Unit = {

    case class kafka_format(date_time: String, keyword_list: String)

    val spark: SparkSession = SparkSession
      .builder()
      .appName("Structrued-Streaming")
      .master("local[2]")
      .getOrCreate()

    import spark.implicits._

    val kafka_df: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "weibo_keyword")
      .option("startingOffsets", "earliest")
      .option("includeTimestamp", value = true)
//      .option("endingOffsets", "latest")
//      .option("startingOffsets", """{"topic1":{"0":23,"1":-2},"topic2":{"0":-2}}""")
//      .option("endingOffsets", """{"topic1":{"0":50,"1":-1},"topic2":{"0":-1}}""")
      .load()

    val keyvalue_df: DataFrame = kafka_df
      .selectExpr("CAST(value AS STRING)")
      .as[String]
      .map((x: String) => {
        val date_time: String = JSON.parseObject(x).getString("datetime")
        val keyword_list: String = JSON.parseObject(x).getString("keywordList")
        (date_time, keyword_list)
      })
      .flatMap((x: (String, String)) =>{
        x._2.split(",").map((word: String) =>(x._1,word))
      })
      .toDF("date_time", "keyword")
      .groupBy("keyword").count()
      .orderBy($"count".desc)


    val query: StreamingQuery = keyvalue_df.writeStream
      .outputMode("complete") //append
      .format("console")
      .start()
//      -------------------------------------------
//      Batch: 0
//      -------------------------------------------
//      +------------+-----+
//      |     keyword|count|
//      +------------+-----+
//      |          事情|  105|
//      |          宋茜|   70|
//      |          美国|   66|
//      |          后浪|   60|
//      |          青春|   58|
//      |          疫情|   48|
//      |          女生|   47|
//      |          日本|   44|
//      |          综艺|   43|
//      |          黑人|   42|
//      |        宋茜点评|   41|
//      |          封面|   41|
//      |        美国疫情|   39|
//      |          成绩|   39|
//      |如果班里某科成绩普遍不好|   38|
//      |          黄河|   38|
//      |          女子|   37|
//      |黄河落水女子怀孕5个多月|   37|
//      |          同桌|   37|
//      |          床上|   36|
//      +------------+-----+
//      -------------------------------------------
//      Batch: 28
//      -------------------------------------------
//      +------------+-----+
//      |     keyword|count|
//      +------------+-----+
//      |          事情|  106|
//      |          宋茜|   70|
//      |          美国|   67|
//      |          后浪|   60|
//      |          青春|   59|
//      |          女生|   52|
//      |          封面|   51|
//      |          疫情|   48|
//      |          孩子|   48|
//      |          日本|   44|
//      |          综艺|   43|
//      |          黑人|   42|
//      |        宋茜点评|   41|
//      |         周扬青|   40|
//      |        美国疫情|   39|
//      |          成绩|   39|
//      |如果班里某科成绩普遍不好|   38|
//      |          黄河|   38|
//      |          火灾|   37|
//      |          女子|   37|
//      +------------+-----+



    query.awaitTermination()

  }
}
