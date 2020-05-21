import com.alibaba.fastjson.JSON
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{DataFrame, SparkSession}

object structured_kafka_append {
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
      .option("startingOffsets", "latest")// earliest
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
//    Append output mode not supported when there are streaming aggregations on streaming DataFrames/DataSets without watermark;;

    val query: StreamingQuery = keyvalue_df.writeStream
      .outputMode("append") //append
      .format("console")
      .start()



    query.awaitTermination()

  }
}
