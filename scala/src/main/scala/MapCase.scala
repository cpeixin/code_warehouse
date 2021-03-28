import scala.collection.mutable.Map
object MapCase {
  def main(args: Array[String]): Unit = {
    val map = Map("zhangsan"->30, "lisi"->40)
    println(map)
    map.update("lisi",50)
    println(map)


    val map_1 = Map("a"->Map("comment"->10,"show"->20), "b"->Map("comment"->10,"show"->20))
    println(map_1)

    map_1("a").update("comment",map_1("a")("comment")+100)
    println(map_1)

  }
}
