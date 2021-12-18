package rdd

/**
 * @author Congpeixin
 * @date 2021/11/27 12:33 下午
 * @version 1.0
 * @describe
 */
object filterNotCase {
  def main(args: Array[String]): Unit = {
//    val target1List = List("1234")
    val target0List = List("")

    val a = target0List.reduceLeft((a,b)=>a+"|"+b)
    val b = target0List.mkString("|")
    println(a)
    println(b)


//
//    target0List.filterNot(record=>target1List.contains(record)).foreach(println)
//    var site1 = "Runoob" :: ("Google" :: ("Baidu" :: Nil))
//    val site2 = "Facebook" :: ("Taobao" :: Nil)

//    // 使用 ::: 运算符
//    var fruit = site1 ::: site2
//    println( "site1 ::: site2 : " + fruit )

    // 使用 List.:::() 方法
//    site1 = site1.:::(site2)
//    println( "site1.:::(site2) : " + site1 )

//    // 使用 concat 方法
//    fruit = List.concat(site1, site2)
//    println( "List.concat(site1, site2) : " + fruit  )


  }
}
