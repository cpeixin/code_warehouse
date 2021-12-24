package RPC.SparkDag.service;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/22 9:00 上午
 * @describe
 */
public interface SparkDagService {
    String submitJob(String jobName);

    String generateDAG() throws InterruptedException;

    String dagSchedule() throws InterruptedException;

    String taskSchedule();

    String stage2Task();

    String completedJob(String jobName);
}
