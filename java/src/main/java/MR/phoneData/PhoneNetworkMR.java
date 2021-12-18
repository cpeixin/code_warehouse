package MR.phoneData;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/7/13 9:35 上午
 * @describe
 */
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.Text;

public class PhoneNetworkMR {

    public static void main(String[] args)throws Exception{
        Job job = new Job();
        job.setJarByClass(PhoneNetworkMR.class);
        job.setJobName("PhoneNetworkMR");

        FileInputFormat.addInputPath(job,new Path("/Users/dongqiudi/IdeaProjects/code_warehouse/java/src/main/java/MR/HTTP_20130313143750.dat"));
        FileOutputFormat.setOutputPath(job,new Path("/Users/dongqiudi/IdeaProjects/code_warehouse/java/src/main/java/MR/res"));


        job.setMapperClass(PhoneNetworkMap.class);
        job.setReducerClass(PhoneNetworkReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);


        System.exit(job.waitForCompletion(true) ? 0 :1);
    }
}