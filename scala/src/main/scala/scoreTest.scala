import org.apache.spark.sql.SparkSession

/**
 * @author Congpeixin
 * @date 2021/12/3 8:36 上午
 * @version 1.0
 * @describe
 */
object scoreTest {
  def main(args: Array[String]): Unit = {
    val spark: SparkSession = SparkSession
      .builder()
      .appName("test-hive-context")
      .master("local[4]")
      .getOrCreate()


    val dataSeq = Seq((1,"a",34),(1,"b",39),(0,"c",33),(0,"d",32),(1,"e",37),(1,"g",34),(1,"e",39),(0,"q",33),(0,"i",32),(1,"f",37),(1,"m",34),(1,"n",39),(0,"u",33),(0,"t",32),(1,"p",37))
    val adf = spark.createDataFrame(dataSeq).toDF("target","name","score")

    val target1 = adf.where("target=1")
    target1.show(50)
    val target0 = adf.where("target=0")
    target0.show(50)
    val allDF = target1.union(target0)
    allDF.show(50)

    val sortedDF = allDF.withColumn("random", col = allDF("target")+scala.util.Random.nextInt(1000)).orderBy("random")
    sortedDF.show(50)

  }
}
