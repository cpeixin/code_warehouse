package data_set;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple3;

import java.util.ArrayList;
import java.util.List;

public class max_func {
    public static void main(String[] args){
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        List data = new ArrayList<Tuple3<Integer,Integer,Integer>>();
        data.add(new Tuple3<>(0,1,0));
        data.add(new Tuple3<>(0,1,1));
        data.add(new Tuple3<>(0,2,2));
        data.add(new Tuple3<>(0,1,3));
        data.add(new Tuple3<>(1,2,5));
        data.add(new Tuple3<>(1,2,9));
        data.add(new Tuple3<>(1,2,11));
        data.add(new Tuple3<>(1,2,13));
        DataSource dataSource = env.fromCollection(data);


    }
}
