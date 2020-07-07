package write

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.sql.types._

object spark_2_hive {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .master("local")
      .appName("login_data_2_hive")
      .config("spark.sql.session.timeZone", "UTC")
      .config("spark.serializer","org.apache.spark.serializer.KryoSerializer")
      .config("hive.exec.dynamici.partition",true)
      .config("hive.exec.dynamic.partition.mode","nonstrict")
      .enableHiveSupport()
      .getOrCreate()

    //  利用withColumn方法，新增列的过程包含在udf函数中
    val to_date_udf: (Long => String) = (timestamps: Long) => {
      var sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
      var date: String = sdf.format(new Date(timestamps*1000L))
      date
    }
    val timestamps_2_date: UserDefinedFunction = udf(to_date_udf)



    //预定义数据格式
    val user_behavior_Schema = new StructType(Array(
      StructField("user_id", LongType, nullable = false),
      StructField("good_id", LongType, nullable = true),
      StructField("good_category_id", LongType, nullable = true),
      StructField("behavior_type", StringType, nullable = true),
      StructField("timestamps", LongType, nullable = true)
    ))


    val user_behavior_dataframe: DataFrame = spark.read.format("csv")
      .option("header", "false") // 文件中的第一行是否为列的名称
      .option("mode", "FAILFAST") // 是否快速失败
      .option("inferSchema", "true") // 是否自动推断 schema
      .schema(user_behavior_Schema)
      .load("/Users/cpeixin/IdeaProjects/code_warehouse/data/UserBehavior_5000.csv")
      .withColumn("snapshot_date", timestamps_2_date(col("timestamps")))

    user_behavior_dataframe
      .write
      .format("Hive")
      .mode(SaveMode.Overwrite)
      .partitionBy("snapshot_date")
      .saveAsTable("test.t_user_behavior")
//    user_behavior_dataframe.show()

  }


}
