package SparkListenerBus

import org.apache.spark.scheduler._

/**
 * @author Congpeixin
 * @date 2021/11/25 9:20 上午
 * @version 1.0
 * @describe
 */
class MySparkListener extends SparkListener {
  override def onApplicationEnd(applicationEnd: SparkListenerApplicationEnd) {
    println("*************************************************")
    println("app:end")
    println("*************************************************")
  }

  override def onJobEnd(jobEnd: SparkListenerJobEnd) {
    println("*************************************************")
    println("job:end")
    jobEnd.jobResult match {
      case JobSucceeded =>
        println("job:end:JobSucceeded")
      case JobFailed(exception) =>
        println("job:end:file")
        exception.printStackTrace()
    }
    println("*************************************************")
  }
}
