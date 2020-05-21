object objectCase {

}


class ApplyTest{

  val name="clow";
  def apply()  {
    println("class ApplyTest--apply()...");
  }

}

//object下的成员默认都是静态的
object ApplyTest{
  def apply() = {
    println("object ApplyTest--apply()...");
    new ApplyTest()
  }
}


object Basic4 {
  def main(args: Array[String]) {
    //类名()->调用了对应object下的apply方法
    var a1=ApplyTest()
    println(a1.name)
    //对象名()->调用了对应class的apply方法
    a1() //输出:class ApplyTest--apply()...
  }
}