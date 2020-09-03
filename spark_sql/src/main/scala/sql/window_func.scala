package sql

import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object window_func {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("")
      .master("local[2]")
      .getOrCreate()


    val row_df: DataFrame = spark.read.json("hdfs://localhost:8020/data/row_range.json")

    row_df.createOrReplaceTempView("table")
    // 写法：1
    spark.sql("select key, num, sum(num) over(partition by key order by num range between 2 following and 20 following) as sum from table")
      .show()

    // 写法：2
    val windowSlide: WindowSpec = Window
      .partitionBy("key")
      .orderBy("num")
      .rangeBetween(Window.currentRow + 2, Window.currentRow + 20)
     
    row_df
      .select(col("key"),sum("num").over(windowSlide))
      .sort("key")
      .show()














    val student_grade_df: DataFrame = spark.read.json("hdfs://localhost:8020/data/student_grade.json")

    // 计算需求：每个学生单科最佳成绩以及成绩所在的年份

    // 定义窗口函数
    import spark.implicits._
    val window: WindowSpec = Window
      .partitionBy("name", "subject")
      .orderBy(student_grade_df("grade").desc)

    val res_df: DataFrame = student_grade_df
      .select(student_grade_df("name"),
        student_grade_df("subject"),
        student_grade_df("year"),
        student_grade_df("grade"),
        row_number().over(window).alias("rank_num")
      ).where("rank_num = 1")

//    res_df.show(100)

    """
      |+-------+-------+----+-----+--------+
      ||   name|subject|year|grade|rank_num|
      |+-------+-------+----+-----+--------+
      || Justin|Chinese|2015|   85|       1|
      || Justin|Chinese|2016|   76|       2|
      || Justin|Chinese|2017|   75|       3|
      ||Michael|Chinese|2015|   95|       1|
      ||Michael|Chinese|2017|   92|       2|
      ||Michael|Chinese|2016|   87|       3|
      ||  Berta|   math|2017|   87|       1|
      ||  Berta|   math|2015|   85|       2|
      ||  Berta|   math|2016|   81|       3|
      ||   Andy|Chinese|2015|   91|       1|
      ||   Andy|Chinese|2016|   90|       2|
      ||   Andy|Chinese|2017|   87|       3|
      ||Michael|   math|2017|   96|       1|
      ||Michael|   math|2016|   68|       2|
      ||Michael|   math|2015|   63|       3|
      ||  Berta|Chinese|2015|   77|       1|
      ||  Berta|Chinese|2016|   74|       2|
      ||  Berta|Chinese|2017|   62|       3|
      ||   Andy|   math|2015|   99|       1|
      ||   Andy|   math|2017|   98|       2|
      ||   Andy|   math|2016|   95|       3|
      || Justin|   math|2016|   87|       1|
      || Justin|   math|2015|   79|       2|
      || Justin|   math|2017|   78|       3|
      |+-------+-------+----+-----+--------+
    """.stripMargin


    // 结果
    """
      |+-------+-------+----+-----+--------+
      ||   name|subject|year|grade|rank_num|
      |+-------+-------+----+-----+--------+
      || Justin|Chinese|2015|   85|       1|
      ||Michael|Chinese|2015|   95|       1|
      ||  Berta|   math|2017|   87|       1|
      ||   Andy|Chinese|2015|   91|       1|
      ||Michael|   math|2017|   96|       1|
      ||  Berta|Chinese|2015|   77|       1|
      ||   Andy|   math|2015|   99|       1|
      || Justin|   math|2016|   87|       1|
      |+-------+-------+----+-----+--------+
    """.stripMargin

  }
}
