import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object scala_rules {
  def main(args: Array[String]): Unit={
    //创建集合
    val _list = List("a", "b", "c")
    val _map = Map("a" -> 1, "b" -> 2, "c" -> 3)
    val _set = Set("a", "b", "c")
    var _array = new Array[String](3)
    val _arraybuffer = new ArrayBuffer[String]()
    //创建队列
    val _queue = new mutable.Queue[Int]

    //求长度
    val len_array: Int = _array.length

    //根据下边取值
    println(_array.head)
    println(_array(1))
    println(_set.head)
    println(_map.head)

    //根据key取值
    println(_map("a"))
    println(_map.get("a"))

    //if else
    if (_map("a") == 1){
      println("OK")
    }else{
      println("No")
    }
    println("=========1===========")
    // for循环
    // until 不包括 尾
    for (i <- 0 until 10){
      println(i)
    }
    println("=========2===========")
    // to 包括 尾
    for (i <- 0 to 10){
      println(i)
    }
    println("=========3===========")
      for (i <- _list.indices){
        println(i)
        _array(i) = _list(i)
    }

    println("=========4===========")
    for (i <- _array
         if i != "a"){
      println(i)
    }
    println("=========5===========")
    for (i <- _array.indices){
      println(_array(i))
    }
    println("=========6===========")
    // 导入以下包
    import scala.util.control._

    // 创建 Breaks 对象
    val loop = new Breaks

    // 在 breakable 中循环
    loop.breakable{
      // 循环
      for(i <- _list.indices){
        if (_array(i).equals("b")){
          // 循环中断
          loop.break
        }
        println(_array(i))
        println("no equal")
      }
    }
    println("=========7===========")
    for (i <- 8 until 15){
      _queue.enqueue(i)
    }
    println(_queue.dequeue())
    println(_queue.dequeue())
  }
}
