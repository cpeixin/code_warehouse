package data_stream.join_case;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import java.util.ArrayList;
import java.util.List;

class StreamingDemo {
    public static void main(String[] args) throws Exception {

        EnvironmentSettings bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamExecutionEnvironment bsEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment bsTableEnv = StreamTableEnvironment.create(bsEnv, bsSettings);

        SingleOutputStreamOperator<MyStreamingSource.Item> source = bsEnv.addSource(new MyStreamingSource()).map(new MapFunction<MyStreamingSource.Item, MyStreamingSource.Item>() {
            @Override
            public MyStreamingSource.Item map(MyStreamingSource.Item item) throws Exception {
                return item;
            }
        });

        DataStream<MyStreamingSource.Item> evenSelect = source.split(new OutputSelector<MyStreamingSource.Item>() {
            @Override
            public Iterable<String> select(MyStreamingSource.Item value) {
                List<String> output = new ArrayList<>();
                if (value.getId() % 2 == 0) {
                    output.add("even");
                } else {
                    output.add("odd");
                }
                return output;
            }
        }).select("even");

        DataStream<MyStreamingSource.Item> oddSelect = source.split(new OutputSelector<MyStreamingSource.Item>() {
            @Override
            public Iterable<String> select(MyStreamingSource.Item value) {
                List<String> output = new ArrayList<>();
                if (value.getId() % 2 == 0) {
                    output.add("even");
                } else {
                    output.add("odd");
                }
                return output;
            }
        }).select("odd");


        bsTableEnv.createTemporaryView("evenTable", evenSelect, "name,id");
        bsTableEnv.createTemporaryView("oddTable", oddSelect, "name,id");

        Table queryTable = bsTableEnv.sqlQuery("select a.id,a.name,b.id,b.name from evenTable as a join oddTable as b on a.name = b.name");

        queryTable.printSchema();

        bsTableEnv.toRetractStream(queryTable, TypeInformation.of(new TypeHint<Tuple4<Integer,String,Integer,String>>(){})).print();

        bsEnv.execute("streaming sql job");
    }

}
