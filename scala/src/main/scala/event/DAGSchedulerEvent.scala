package event

/**
 * @author Congpeixin
 * @date 2021/11/23 8:04 上午
 * @version 1.0
 * @describe
 */
private sealed trait DAGSchedulerEvent


/** A result-yielding job was submitted on a target RDD */
private case class JobSubmitted(jobId: Int)
  extends DAGSchedulerEvent
