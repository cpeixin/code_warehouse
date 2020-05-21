package data_stream;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


import static jdk.nashorn.internal.objects.NativeMath.random;

/**
 *
 * 写入HBase
 * 继承RichSinkFunction重写父类方法
 *
 * 写入hbase时500条flush一次, 批量插入, 使用的是writeBufferSize
 */
class HBaseWriter extends RichSinkFunction<datastream_2_hbase.Raw>{
    private static final Logger logger = LoggerFactory.getLogger(HBaseWriter.class);

    private static org.apache.hadoop.conf.Configuration configuration;
    private static Connection connection = null;
    private static BufferedMutator mutator;
    private static int count = 0;

    @Override
    public void open(Configuration parameters) throws Exception {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.master", "localhost:60020");
        configuration.set("hbase.zookeeper.quorum", "localhost");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        try {
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedMutatorParams params = new BufferedMutatorParams(TableName.valueOf("t_weibo_keyword_2"));
//        params.writeBufferSize(2 * 1024 * 1024);
        mutator = connection.getBufferedMutator(params);
    }

    @Override
    public void close() throws IOException {
        if (mutator != null) {
            mutator.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void invoke(datastream_2_hbase.Raw values, Context context) throws Exception {

        String RowKey = values.date_time() + String.valueOf(random(100));
        String Key = "date_time";
        String Value = values.date_time();
        String Key_1 = "keywordList";
        String Value_1 = values.keywordList();
        System.out.println("Column Family=cf1,  RowKey=" + RowKey + ", Key=" + Key + " ,Value=" + Value);
        System.out.println("Column Family=cf1,  RowKey=" + RowKey + ", Key=" + Key_1 + " ,Value=" + Value_1);
        Put put = new Put(RowKey.getBytes());
        put.addColumn("cf1".getBytes(), Key.getBytes(), Value.getBytes());
        put.addColumn("cf1".getBytes(), Key_1.getBytes(), Value_1.getBytes());
        mutator.mutate(put);
        //每满500条刷新一下数据
        if (count >= 5){
            mutator.flush();
            count = 0;
        }
        count = count + 1;
    }
}
