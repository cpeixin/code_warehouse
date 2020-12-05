package pvuv

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer

object PvUvProcess {
  def main(args: Array[String]): Unit = {
    val stream_env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // default is EXACTLY_ONCE
    stream_env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
    stream_env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    // default interval is 10L
    stream_env.enableCheckpointing(5000)

    val properties = new Properties()

    properties.setProperty("bootstrap.servers", "127.0.0.1:9092")
    // 如果你是0.8版本的Kafka，需要配置
    //properties.setProperty("zookeeper.connect", "localhost:2181");
    //设置消费组
    properties.setProperty("group.id", "group_test")

    val kafkaStream: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer("", new SimpleStringSchema, properties)
    kafkaStream.setStartFromLatest()



  }
}
