package data_set

import org.apache.flink.api.scala.{AggregateDataSet, DataSet, ExecutionEnvironment}
import org.apache.flink.api.scala._


object wordcount {
  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    val word_dataset: DataSet[String] = env.readTextFile("/Users/cpeixin/IdeaProjects/code_warehouse/data/word.txt")

    val keyvalue_dataset: DataSet[(String, Int)] = word_dataset.flatMap((_: String).split(" ")).map { word: String =>(word, 1)}

    val result: AggregateDataSet[(String, Int)] = keyvalue_dataset.groupBy(0).sum(1)

    result.print()

  }

}
