package MR.phoneData;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/7/13 9:33 上午
 * @describe
 */
import MR.phoneData.FlowBean;
import org.apache.hadoop.io.IntWritable;

import org.apache.hadoop.io.LongWritable;

import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class PhoneNetworkMap extends Mapper<LongWritable,Text,Text, FlowBean> {
    @Override
    public void map(LongWritable key, Text value, Context context)throws IOException,InterruptedException{

        String line = value.toString();
        String[] str = line.split("\t");

        context.write(new Text(str[0]), new FlowBean(Long.parseLong(str[8]), Long.parseLong(str[9])));

    }
}