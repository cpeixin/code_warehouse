object wordCount {
  def main(args: Array[String]): Unit ={
    val wordList = List("hello world hello")
    val word_array = wordList.flatMap(_.split(" "))
    val word_kv = word_array.map((_,1))
    val word_group = word_kv.groupBy(_._1)
    val result = word_group.map(x => (x._1, x._2.size))
    println(result)
  }
}
