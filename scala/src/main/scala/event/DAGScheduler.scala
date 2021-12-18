package event

import org.apache.spark.internal.Logging

/**
 * @author Congpeixin
 * @date 2021/11/22 8:12 上午
 * @version 1.0
 * @describe
 */
private class DAGScheduler()
  extends Logging {


  private val eventProcessLoop = new DAGSchedulerEventProcessLoop(this)


  def submitJob[T, U](): Unit = {
    val jobId = 1
    eventProcessLoop.post(JobSubmitted(jobId))
  }


  def runJob[T, U](): Unit = {
    submitJob()
  }


  def handleJobSubmitted(jobId: Int): Unit = {
    println("handleJobSubmitted" + " 提交 Job 1")
  }


  eventProcessLoop.start()
}


private class DAGSchedulerEventProcessLoop(dagScheduler: DAGScheduler)
  extends EventLoop[DAGSchedulerEvent]("dag-scheduler-event-loop") with Logging {


  /**
   * The main event loop of the DAG scheduler.
   */
  override def onReceive(event: DAGSchedulerEvent): Unit = {
    try {
      println("doOnReceive event " + event.toString)
      doOnReceive(event)
    } finally {
      println("doOnReceive over")
    }
  }


  private def doOnReceive(event: DAGSchedulerEvent): Unit = event match {
    case JobSubmitted(jobId) =>
      dagScheduler.handleJobSubmitted(jobId)
  }


  override def onError(e: Throwable): Unit = {
    logError("DAGSchedulerEventProcessLoop failed; shutting down SparkContext", e)
  }


  override def onStop(): Unit = {
    // Cancel any active jobs in postStop hook
    logWarning("Cancel any active jobs in postStop hook")  }
}


private object DAGScheduler {
  // The time, in millis, to wait for fetch failure events to stop coming in after one is detected;
  // this is a simplistic way to avoid resubmitting tasks in the non-fetchable map stage one by one
  // as more failure events come in
  val RESUBMIT_TIMEOUT = 200


  // Number of consecutive stage attempts allowed before a stage is aborted
  val DEFAULT_MAX_CONSECUTIVE_STAGE_ATTEMPTS = 4
}