package RPC.SparkDag.service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/22 9:08 上午
 * @describe
 */
public class SparkDagServiceImpl implements SparkDagService{
    @Override
    public String submitJob(String jobName) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = df.format(new Date());
        return datetime+"   submit job:  "+jobName;
    }

    @Override
    public String generateDAG() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = df.format(new Date());
        return datetime + "  generateDAG";
    }

    @Override
    public String dagSchedule() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = df.format(new Date());
        return datetime + "  dagSchedule";
    }

    @Override
    public String taskSchedule() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = df.format(new Date());
        return datetime + "   taskSchedule";
    }

    @Override
    public String stage2Task() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = df.format(new Date());
        return datetime + "   stage2Task";
    }

    @Override
    public String completedJob(String jobName) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = df.format(new Date());
        return datetime + "   completedJob";
    }
}
