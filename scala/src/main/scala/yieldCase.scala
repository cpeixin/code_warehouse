object yieldCase {
  def main(args: Array[String]): Unit = {
    val name_list = List("brent","zander","vicky","haylee","vip","cloud","bret","sky")

    val friend_list = for (i <- name_list
        if i.startsWith("b")
    )yield i

    println(friend_list)

  }
}
