import scala.collection.mutable.ListBuffer

object test_list {
  def main(args: Array[String]): Unit = {

    var gradeList: ListBuffer[Int] = ListBuffer[Int](1,2,3)
    println(gradeList.toList)
  }
}
