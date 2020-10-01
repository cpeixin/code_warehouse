package data_set;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class wordcount_java {
    public static void main(String[] args) throws Exception {
        // 创建Flink运行的上下文环境
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 创建数据集
        DataSet<String> data = env.fromElements(
                "Flink Spark Storm", "Flink Flink Flink", "Spark Spark Spark", "Storm Storm Storm"
        );

        AggregateOperator<Tuple2<String, Integer>> sum = data.flatMap(new LineSplitter()).groupBy(0).sum(1);

        sum.printToErr();

    }


    public static class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>>{
        @Override
        public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
               String[] values = s.split("\\W+");
               for (String value: values){
                   if (value.length() > 0){
                       collector.collect(new Tuple2<String, Integer>(value, 1));
                   }
               }
        }
    }

}
