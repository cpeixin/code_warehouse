package distributeCache;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 分布式缓存
 *  第一步：首先需要在 env 环境中注册一个文件，该文件可以来源于本地，也可以来源于 HDFS ，并且为该文件取一个名字。
 *  第二步：在使用分布式缓存时，可根据注册的名字直接获取。
 */

public class DistributeCache {
    public static void main(String[] args) throws Exception {

        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.registerCachedFile("/Users/cpeixin/cache/distributedcache.txt", "distributedCache");
        //1：注册一个文件,可以使用hdfs上的文件 也可以是本地文件进行测试
        DataSource<String> data = env.fromElements("Linea", "Lineb", "Linec", "Lined");

        // RichFuction除了提供原来MapFuction的方法之外，还提供open, close, getRuntimeContext 和setRuntimeContext方法，
        // 这些功能可用于参数化函数（传递参数），创建和完成本地状态，访问广播变量以及访问运行时信息以及有关迭代中的信息。
        DataSet<String> result = data.map(new RichMapFunction<String, String>() {
            private ArrayList<String> dataList = new ArrayList<String>();

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                //2：使用该缓存文件
                File myFile = getRuntimeContext().getDistributedCache().getFile("distributedCache");
                List<String> lines = FileUtils.readLines(myFile);
                for (String line : lines) {
                    dataList.add(line);
                    System.err.println("分布式缓存为:" + line);
                }
            }

            @Override
            public String map(String value) throws Exception {
                //在这里就可以使用dataList
                System.err.println("使用datalist：" + dataList + "-------" +value);
                //业务逻辑
                return dataList +"：" +  value;
            }
        });
        result.printToErr();
    }

}
