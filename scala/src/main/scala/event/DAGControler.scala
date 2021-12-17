package event

/**
 * @author Congpeixin
 * @date 2021/11/23 8:06 上午
 * @version 1.0
 * @describe
 */
object DAGControler {
  def main(args: Array[String]): Unit = {
    var _dagScheduler: DAGScheduler = new DAGScheduler
    def dagScheduler: DAGScheduler = _dagScheduler

    dagScheduler.runJob()
  }
}
