package dataset

import org.apache.spark.sql.{Dataset, SparkSession}

object dataset {
  case class Person(name: String, age: Long)
  def main(args: Array[String]): Unit ={
    val spark: SparkSession = SparkSession
      .builder()
      .appName("dataset_case")
      .master("local")
      .getOrCreate()

    import spark.implicits._
    // $example on:create_ds$
    // Encoders are created for case classes
    val caseClassDS: Dataset[Person] = Seq(Person("Andy", 32)).toDS()
    caseClassDS.show()
    // +----+---+
    // |name|age|
    // +----+---+
    // |Andy| 32|
    // +----+---+

    // Encoders for most common types are automatically provided by importing spark.implicits._
    val primitiveDS: Dataset[Int] = Seq(1, 2, 3).toDS()
    primitiveDS.map(_ + 1).collect() // Returns: Array(2, 3, 4)

    // DataFrames can be converted to a Dataset by providing a class. Mapping will be done by name
    val path = "examples/src/main/resources/people.json"
    val peopleDS: Dataset[Person] = spark.read.json(path).as[Person]

    peopleDS.sort()
    peopleDS.show()
    // +----+-------+
    // | age|   name|
    // +----+-------+
    // |null|Michael|
    // |  30|   Andy|
    // |  19| Justin|
    // +----+-------+
    // $example off:create_ds$
  }
}
