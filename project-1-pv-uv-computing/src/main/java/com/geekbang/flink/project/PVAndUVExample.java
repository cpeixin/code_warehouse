package com.geekbang.flink.project;

import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.elasticsearch.ActionRequestFailureHandler;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch7.ElasticsearchSink;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumerBase;
import org.apache.flink.util.Collector;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.io.Serializable;
import java.util.*;

import static com.geekbang.flink.project.Constants.WS_URL;

@Slf4j
public class PVAndUVExample {

    public static void main(String[] args) throws Exception {
        //Flink 解析参数
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String kafkaTopic = parameterTool.get("kafka-topic","pvuv");
        String brokers = parameterTool.get("brokers", "localhost:9092");
        System.out.printf("Reading from kafka topic %s @ %s\n", kafkaTopic, brokers);
        System.out.println();

        Properties kafkaProps = new Properties();
        kafkaProps.setProperty("bootstrap.servers", brokers);
        // 设置每10秒去检测新增的动态分区
        kafkaProps.setProperty(FlinkKafkaConsumerBase.KEY_PARTITION_DISCOVERY_INTERVAL_MILLIS, "10");
        //kafkaProps.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        FlinkKafkaConsumer010<UserBehaviorEvent> kafka = new FlinkKafkaConsumer010<>(kafkaTopic, new UserBehaviorEventSchema(), kafkaProps);
        kafka.setStartFromLatest(); // Latest , Earliest, Group, FromTimeStamp, setStartFromGroupOffsets(default)
//        kafka.setStartFromEarliest();
        kafka.setCommitOffsetsOnCheckpoints(false); //kafka消费offset提交配置, 默认是true

        //创建环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        kafka.assignTimestampsAndWatermarks(
                WatermarkStrategy
                        .forGenerator((ctx) -> new PeriodicWatermarkGenerator())
                        .withTimestampAssigner((ctx) -> new TimeStampExtractor())); // 抽取事件事件方法

        DataStreamSource<UserBehaviorEvent> dataStreamByEventTime = env.addSource(kafka);

        DataStream<Tuple4<Long, Long, Long, Integer>> uvCounter = dataStreamByEventTime
                .windowAll(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(3)))
                .allowedLateness(Time.minutes(5))
                .process(new ProcessAllWindowFunction<UserBehaviorEvent, Tuple4<Long, Long, Long, Integer>, TimeWindow>() {
                    @Override
                    public void process(Context context, Iterable<UserBehaviorEvent> elements, Collector<Tuple4<Long, Long, Long, Integer>> out) {
                        Long pv = 0L;
                        Set<Integer> userIds = new HashSet<>();
                        for (UserBehaviorEvent userBehavior : elements) {
                            pv++;
                            userIds.add(userBehavior.getUserId());
                        }
                        TimeWindow window = context.window();
                        out.collect(new Tuple4<>(window.getStart(), window.getEnd(), pv, userIds.size()));
                    }
                });

        uvCounter.print().setParallelism(1);

        List<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost("localhost", 9200, "http"));

        ElasticsearchSink.Builder<Tuple4<Long, Long, Long, Integer>> esSinkBuilder = new ElasticsearchSink.Builder<>(
                httpHosts,
                (Tuple4<Long, Long, Long, Integer> element, RuntimeContext ctx, RequestIndexer indexer) -> {
                    indexer.add(createIndexRequest(element, parameterTool));
                });

        esSinkBuilder.setFailureHandler(
                new CustomFailureHandler(parameterTool.get("pvuv", "pvuv-20201122")));

        // this instructs the sink to emit after every element, otherwise they would be buffered
        //批量请求的配置；这将指示接收器在每个元素之后发出请求，否则将对它们进行缓冲。
        esSinkBuilder.setBulkFlushMaxActions(1);

        uvCounter.addSink(esSinkBuilder.build());

        uvCounter.addSink(new WebsocketSink(WS_URL));

        env.execute(parameterTool.get("appName", "PVAndUVExample"));

    }

    private static class PeriodicWatermarkGenerator implements WatermarkGenerator<UserBehaviorEvent>, Serializable {

        private long currentWatermark = Long.MIN_VALUE;

        @Override
        public void onEvent(
                UserBehaviorEvent event, long eventTimestamp, WatermarkOutput output) {
            currentWatermark = eventTimestamp;
        }

        @Override
        public void onPeriodicEmit(WatermarkOutput output) {
            long effectiveWatermark =
                    currentWatermark == Long.MIN_VALUE ? Long.MIN_VALUE : currentWatermark - 1;
            output.emitWatermark(new Watermark(effectiveWatermark));
        }
    }

    private static class TimeStampExtractor implements TimestampAssigner<UserBehaviorEvent> {
        @Override
        public long extractTimestamp(UserBehaviorEvent element, long recordTimestamp) {
            return element.getTs();
        }
    }

    private static class CustomFailureHandler implements ActionRequestFailureHandler {

        private static final long serialVersionUID = 942269087742453482L;

        private final String index;

        CustomFailureHandler(String index) {
            this.index = index;
        }

        @Override
        public void onFailure(ActionRequest action, Throwable failure, int restStatusCode, RequestIndexer indexer) throws Throwable {
            if (action instanceof IndexRequest) {
                Map<String, Object> json = new HashMap<>();
                json.put("data", ((IndexRequest) action).source());

                indexer.add(
                        Requests.indexRequest()
                                .index(index)
                                .id(((IndexRequest) action).id())
                                .source(json));
            } else {
                throw new IllegalStateException("unexpected");
            }
        }
    }

    private static IndexRequest createIndexRequest(Tuple4<Long, Long, Long, Integer> element, ParameterTool parameterTool) {
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("window_start", element.f0);
        json.put("window_end", element.f1);
        json.put("pv", element.f2);
        json.put("uv", element.f3);
        String index = parameterTool.get("index","pvuv-20201122");
        return Requests.indexRequest()
                .index(index)
                .type("default")
                .id(element.f1.toString())
                .source(json);
    }

}