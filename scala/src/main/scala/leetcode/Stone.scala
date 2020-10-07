package leetcode

import scala.collection.mutable

object Stone {
  def numJewelsInStones(J: String, S: String): Unit = {
    var count = 0
    // 创建集合
    val stone_map = new scala.collection.mutable.HashMap[Char, Int]
    for (i <- 0 until S.length) {
      stone_map.put(S.charAt(i), stone_map.getOrElse(S.charAt(i),0)+1)
    }

    for (i <- 0 until J.length) {
      if (stone_map.contains(J.charAt(i))) {
          count += stone_map(J.charAt(i))
      }
    }

    count
  }


  def main(args: Array[String]): Unit = {
    val c = List((1, 2), (3, 2), (9, 9), (1, 4), (2, 4), (3, 1))
    val d = mutable.HashMap[Int, Int]()
    for ((x, y) <- c) {
      d.put(x, d.getOrElse(x, 0) + y)
    }

    numJewelsInStones("aA","aaabbbdfasdf")

  }
}
