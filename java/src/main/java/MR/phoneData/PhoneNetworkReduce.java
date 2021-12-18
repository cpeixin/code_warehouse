package MR.phoneData;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/7/13 9:34 上午
 * @describe
 */

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;
import java.io.IOException;

public class PhoneNetworkReduce extends Reducer<Text, FlowBean,Text,FlowBean> {
    @Override
    public void reduce(Text key, Iterable<FlowBean> values, Context context)throws IOException,InterruptedException{
        long up_sum = 0;
        long down_sum = 0;
        for(FlowBean value: values) {
            up_sum += value.getUpFlow();
            down_sum += value.getDownFlow();
        }
        context.write(key,new FlowBean(up_sum, down_sum));
    }
}
