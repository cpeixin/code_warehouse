package data_stream.PV_UV;

import org.apache.flink.api.common.functions.FilterFunction;

public class UserActionFilter implements FilterFunction<String> {
    @Override
    public boolean filter(String input) throws Exception {
        return input.contains("CLICK") && input.startsWith("{") && input.endsWith("}");
    }
}

