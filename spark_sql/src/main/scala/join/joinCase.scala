package join

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.hive.test.TestHive.sparkContext
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}


object joinCase {
  case class sex(name: String, male: String)
  case class age(name: String, age: Int)
  def main(args: Array[String]): Unit ={
    val spark: SparkSession = SparkSession
      .builder()
      .appName("function_case")
      .master("local")
      .config("spark.sql.crossJoin.enabled", "true")
      .getOrCreate()

    import spark.implicits._
    val sexDF = spark.sparkContext
      .makeRDD(List(("brent", "male"), ("haylee", "female"), ("vicky", "male")))
      .map((x: (String, String)) => sex(x._1, x._2)).toDF("name","male")

    val ageDF = spark.sparkContext
      .makeRDD(List(("brent", 23), ("haylee", 25), ("vicky", 26), ("brent", 26)))
      .map((x: (String, Int)) => age(x._1, x._2)).toDF("name","age")

    sexDF.join(ageDF, Seq("name"), "left_anti").show(10)
  }

  def show_get_data(spark: SparkSession, df: DataFrame): Unit = {

    df.printSchema()
    //    root
    //    |-- age: long (nullable = true)
    //    |-- birthday: string (nullable = true)
    //    |-- customer_id: long (nullable = true)
    //    |-- deposit_amount: double (nullable = true)
    //    |-- last_login_time: string (nullable = true)
    //    |-- user_name: string (nullable = true)

    df.show(5)
    //默认打印前20条结果
    //    +---+----------+-----------+--------------+-------------------+---------+
    //    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
    //    +---+----------+-----------+--------------+-------------------+---------+
    //    | 22|1993-04-05|   12031602|        3000.0|2017-03-10 14:55:22|    brent|
    //    | 23|1992-08-10|   12031603|       4000.56|2017-03-11 10:55:00|   haylee|
    //    | 30|2000-03-02|   12031604|         200.4|2017-03-10 09:10:00|    vicky|
    //    +---+----------+-----------+--------------+-------------------+---------+

    // Select only the "name" column
    // 这个表达式不能进行计算操作
    df.select("user_name", "age").show()
    //    +---------+
    //    |user_name|
    //    +---------+
    //    |    brent|
    //    |   haylee|
    //    |    vicky|
    //    +---------+

    // Select everybody, but increment the age by 1
    // This import is needed to use the $-notation
    import spark.implicits._
    df.select($"user_name", $"age" + 1 as "new_age").show()
    //    +---------+-------+
    //    |user_name|new_age|
    //    +---------+-------+
    //    |    brent|     23|
    //    |   haylee|     24|
    //    |    vicky|     31|
    //    +---------+-------+

    import org.apache.spark.sql.functions._
    df.select(col("customer_id"), col("deposit_amount")).show()

    df.limit(5).show()

    df.describe()



  }

  def map_data(spark: SparkSession, df: DataFrame): Unit = {
    import spark.implicits._
    // 注意 这里是Row类型
    df.map((x: Row) => {"name: "+x.getAs[String]("user_name")}).show()

  }

  def filter_data(spark: SparkSession, df: DataFrame): Unit = {
    import spark.implicits._
    // 取等于时必须用===
    df.filter($"user_name" === "brent").show()
//    +---+----------+-----------+--------------+-------------------+---------+
//    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
//    +---+----------+-----------+--------------+-------------------+---------+
//    | 22|1993-04-05|   12031602|        3000.0|2017-03-10 14:55:22|    brent|
//    +---+----------+-----------+--------------+-------------------+---------+
    df.filter($"age" > 25).show()
//    +---+----------+-----------+--------------+-------------------+---------+
//    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
//    +---+----------+-----------+--------------+-------------------+---------+
//    | 30|2000-03-02|   12031604|         200.4|2017-03-10 09:10:00|    vicky|
//    +---+----------+-----------+--------------+-------------------+---------+
    df.filter("deposit_amount = 3000.0").show()
    df.filter($"deposit_amount" > 200 and $"age" < 25).show()
//    +---+----------+-----------+--------------+-------------------+---------+
//    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
//    +---+----------+-----------+--------------+-------------------+---------+
//    | 22|1993-04-05|   12031602|        3000.0|2017-03-10 14:55:22|    brent|
//    | 23|1992-08-10|   12031603|       4000.56|2017-03-11 10:55:00|   haylee|
//    +---+----------+-----------+--------------+-------------------+---------+

    df.filter("substring(user_name,0,1) = 'h'").show()
//    +---+----------+-----------+--------------+-------------------+---------+
//    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
//    +---+----------+-----------+--------------+-------------------+---------+
//    | 23|1992-08-10|   12031603|       4000.56|2017-03-11 10:55:00|   haylee|
//    +---+----------+-----------+--------------+-------------------+---------+

//  在源码中可以看到，where算子，底层是filter实现的。
    import org.apache.spark.sql.functions._
    df.where(col("age") > 23).show()
//    +---+----------+-----------+--------------+-------------------+---------+
//    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
//    +---+----------+-----------+--------------+-------------------+---------+
//    | 30|2000-03-02|   12031604|         200.4|2017-03-10 09:10:00|    vicky|
//    +---+----------+-----------+--------------+-------------------+---------+

    df.where("age> 23").show()
//    +---+----------+-----------+--------------+-------------------+---------+
//    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
//    +---+----------+-----------+--------------+-------------------+---------+
//    | 30|2000-03-02|   12031604|         200.4|2017-03-10 09:10:00|    vicky|
//    +---+----------+-----------+--------------+-------------------+---------+
  }

  def sort_data(spark: SparkSession, df: DataFrame): Unit = {
    import spark.implicits._
    df.sort($"age".desc).show()
//    +---+----------+-----------+--------------+-------------------+---------+
//    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
//    +---+----------+-----------+--------------+-------------------+---------+
//    | 30|2000-03-02|   12031604|         200.4|2017-03-10 09:10:00|    vicky|
//    | 23|1992-08-10|   12031603|       4000.56|2017-03-11 10:55:00|   haylee|
//    | 22|1993-04-05|   12031602|        3000.0|2017-03-10 14:55:22|    brent|
//    +---+----------+-----------+--------------+-------------------+---------+
    df.sort($"age".asc).show()
//    +---+----------+-----------+--------------+-------------------+---------+
//    |age|  birthday|customer_id|deposit_amount|    last_login_time|user_name|
//    +---+----------+-----------+--------------+-------------------+---------+
//    | 22|1993-04-05|   12031602|        3000.0|2017-03-10 14:55:22|    brent|
//    | 23|1992-08-10|   12031603|       4000.56|2017-03-11 10:55:00|   haylee|
//    | 30|2000-03-02|   12031604|         200.4|2017-03-10 09:10:00|    vicky|
//    +---+----------+-----------+--------------+-------------------+---------+

    // 只能对数字类型和日期类型生效
    df.orderBy($"age")

    df.orderBy(- df("age"))

    df.orderBy(df("age").desc)

  }

  def groupBy_data(spark: SparkSession, df: DataFrame): Unit = {
    df.groupBy("age").count().show()
//    +---+-----+
//    |age|count|
//    +---+-----+
//    | 22|    1|
//    | 30|    1|
//    | 23|    1|
//    +---+-----+
    // 只能作用于数值字段
    df.groupBy("user_name").max("deposit_amount").show()
    df.groupBy("user_name").min("deposit_amount").show()
    df.groupBy("user_name").mean("deposit_amount").as("mean_deposit_amount").show()
    df.groupBy("user_name").sum("deposit_amount").toDF("user_name", "sum_deposit_amount").show()
//    +---------+------------------+
//    |user_name|sum_deposit_amount|
//    +---------+------------------+
//    |    vicky|             200.4|
//    |   haylee|           4000.56|
//    |    brent|            3000.0|
//    +---------+------------------+


    import org.apache.spark.sql.functions._
    df.groupBy("user_name", "age")
      .agg(min("deposit_amount").as("min_deposit_amount"))
      .show()
//    +---------+---+------------------+
//    |user_name|age|min_deposit_amount|
//    +---------+---+------------------+
//    |    vicky| 30|             200.4|
//    |   haylee| 23|           4000.56|
//    |    brent| 22|            3000.0|
//    +---------+---+------------------+


    //单独使用 agg
    df.agg("age" -> "max").show()

  }

  def distinct_data(spark: SparkSession, df: DataFrame): Unit = {
    // distinct 底层实现实则为 dropDuplicates（）
    df.distinct()
    df.dropDuplicates()
  }

  def join_data(spark: SparkSession, df: DataFrame, df2: DataFrame): Unit = {
    //笛卡尔积, spark2中默认不开启笛卡尔积，需添加"spark.sql.crossJoin.enabled", "true"配置
    df.join(df2).show()

    df.join(df2, "user_name").show()

    df.join(df2, Seq("user_name"), "left").show()
//    +---------+---+----------+-----------+--------------+-------------------+------+
//    |user_name|age|  birthday|customer_id|deposit_amount|    last_login_time|   sex|
//    +---------+---+----------+-----------+--------------+-------------------+------+
//    |    vicky| 30|2000-03-02|   12031604|         200.4|2017-03-10 09:10:00|  male|
//    |   haylee| 23|1992-08-10|   12031603|       4000.56|2017-03-11 10:55:00|female|
//    |    brent| 22|1993-04-05|   12031602|        3000.0|2017-03-10 14:55:22|  male|
//    +---------+---+----------+-----------+--------------+-------------------+------+

  }


  def intersect_data(spark: SparkSession, df: DataFrame, df2: DataFrame): Unit = {
    // 获取两个DataFrame中共有的记录
    df.intersect(df2).show(false)
  }

  def withColumn_rename_dataframe(spark: SparkSession, df: DataFrame): Unit = {
    // 字段重命名
    df.withColumnRenamed("deposit_amount","withdraw_amount").show()
    // 添加新列
    import spark.implicits._
    df.withColumn("next_year_age", $"age"+1).show()
  }

}