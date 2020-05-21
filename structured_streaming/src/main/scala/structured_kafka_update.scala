import com.alibaba.fastjson.JSON
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{DataFrame, SparkSession}

object structured_kafka_update {
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
      .option("startingOffsets", "latest")
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
      .orderBy($"count".desc)  //在update中，使用不了order by sort排序

    val query: StreamingQuery = keyvalue_df.writeStream
          .outputMode("update") //append
          .format("console")
          .start()

//    -------------------------------------------
//    Batch: 1
//    -------------------------------------------
//      +-----------+-----+
//      |    keyword|count|
//      +-----------+-----+
//      |        李易峰|    3|
//      |         闺蜜|    3|
//      |         珠峰|    1|
//      |         天赋|    1|
//      |         封面|    3|
//      |       中华儿女|    3|
//      |         坏话|    1|
//      |珠峰每年都在向长春移动|    1|
//      |  李易峰中华儿女封面|    3|
//      |         观人|    1|
//      | 有个比自己漂亮的闺蜜|    3|
//      |       根本就是|    1|
//      |         长春|    1|
//      +-----------+-----+

//    -------------------------------------------
//    Batch: 2
//    -------------------------------------------
//      +-----------+-----+
//      |    keyword|count|
//      +-----------+-----+
//      |        李易峰|    4|
//      |         珠峰|    2|
//      |         封面|    4|
//      |       中华儿女|    4|
//      |珠峰每年都在向长春移动|    2|
//      |  李易峰中华儿女封面|    4|
//      |         长春|    2|
//      +-----------+-----+

//    -------------------------------------------
//    Batch: 3
//    -------------------------------------------
//      +------------+-----+
//      |     keyword|count|
//      +------------+-----+
//      | 高晓松约定不与郑钧合影|    4|
//      |          合影|    4|
//      | 康康你的G SHOCK|    1|
//      |         高晓松|    4|
//      |          康康|    1|
//      |          蚊子|    4|
//      |          母亲|    1|
//      |          郑钧|    4|
//      |夏天为了阻挡蚊子做的努力|    4|
//      |          活埋|    1|
//      +------------+-----+

//    -------------------------------------------
//    Batch: 4
//    -------------------------------------------
//      +--------------+-----+
//      |       keyword|count|
//      +--------------+-----+
//      |            韩国|    3|
//      |            男子|    3|
//      | 四川遭男教师性侵男学生发声|    3|
//      |            教师|    3|
//      |            夜店|    3|
//      |            开学|    4|
//      |        开学寝室奇观|    4|
//      |            学生|    3|
//      |            寝室|    4|
//      |            四川|    3|
//      +--------------+-----+

//    -------------------------------------------
//    Batch: 5
//    -------------------------------------------
//      +---------------+-----+
//      |        keyword|count|
//      +---------------+-----+
//      |   蚕豆就像北方冬天的大白菜|    2|
//      |             感觉|    1|
//      |            大白菜|    2|
//      |             潜力|    1|
//      |           剧烈运动|    5|
//      |             灵气|    1|
//      |             机会|    1|
//      |             球员|    1|
//      |             宁波|    2|
//      |             福德|    1|
//      |佩戴高性能口罩剧烈运动有多危险|    5|
//      |             成员|    1|
//      |            俱乐部|    1|
//      |             口罩|    5|
//      |           宁波银行|    2|
//      |             蚕豆|    2|
//      |             核心|    1|
//      |             银行|    2|
//      |             利森|    1|
//      |            高性能|    5|
//      +---------------+-----+

//    -------------------------------------------
//    Batch: 6
//    -------------------------------------------
//    +-------------+-----+
//    |      keyword|count|
//    +-------------+-----+
//    |           荣耀|    5|
//    |           王者|    5|
//    |         工作人员|    2|
//    |          虞书欣|    3|
//    |          特朗普|    2|
//    +-------------+-----+

//    -------------------------------------------
//    Batch: 7
//    -------------------------------------------
//    +-------------+-----+
//    |      keyword|count|
//    +-------------+-----+
//    |           宋茜|    2|
//    |           情侣|    4|
//    |           封面|    6|
//    |         工作人员|    6|
//    |   CP感很强的荧幕情侣|    4|
//    |      宋茜宫廷风封面|    2|
//    |           荧幕|    4|
//    |特朗普一名贴身工作人员确诊|    6|
//    |          特朗普|    6|
//    |           宫廷|    2|
//    +-------------+-----+



    query.awaitTermination()

  }
}
