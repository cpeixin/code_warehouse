package assert_case

object assert_test {
  def main(args: Array[String]): Unit = {
    test_Assert(1)
  }

  /**
    * assert方法有俩种，assert(condition),assert(condition,explanation)
    * 第一个参数是布尔类型，第二个参数是解释语句字符串
    * @param a
    */
  def test_Assert(a:Int): Unit ={
    assert(if(a>0) true else false,"a<0")
  }
}
