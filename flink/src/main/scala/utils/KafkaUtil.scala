package utils

import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaConsumerBase}

object KafkaUtil {

  private val properties = new Properties()

  properties.setProperty("bootstrap.servers", "localhost:9092")
  properties.setProperty("group.id", "kafka_consumer")

  def getKafkaSource(topic: String): FlinkKafkaConsumerBase[String] ={
    val kafkaConsumer = new FlinkKafkaConsumer(topic, new SimpleStringSchema(), properties)
    kafkaConsumer
  }

  def main(args: Array[String]): Unit = {

  }
}
