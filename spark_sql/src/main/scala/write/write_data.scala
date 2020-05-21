package write

import java.util.Properties

import org.apache.spark.sql.{DataFrame, SparkSession}

object write_data {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("write_case")
      .master("local")
      .getOrCreate()

    // read
    val jdbcDF: DataFrame = spark.read
      .format("jdbc")
      .option("url", "jdbc:postgresql:dbserver")
      .option("dbtable", "schema.tablename")
      .option("user", "username")
      .option("password", "password")
      .load()


    // write
    // Saving data to a JDBC source
    jdbcDF.write
      .format("jdbc")
      .option("url", "jdbc:postgresql:dbserver")
      .option("dbtable", "schema.tablename")
      .option("user", "username")
      .option("password", "password")
      .mode("append")
      .save()

    // or
    val properties=new Properties()
    properties.setProperty("user","root")
    properties.setProperty("password","secret_password")
    jdbcDF.write
      .mode("append")
      .jdbc("jdbc:mysql://your_ip:3306/my_test?useUnicode=true&characterEncoding=UTF-8","t_result",properties)

  }
}
