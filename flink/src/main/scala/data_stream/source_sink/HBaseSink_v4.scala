package data_stream.source_sink

import data_stream.source_sink.datastream_2_hbase.Raw
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants, TableName}


class HBaseSink_v4(tableName: String, family: String) extends RichSinkFunction[Raw] {


  var conn: Connection = _

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    val conf = HBaseConfiguration.create()
    conf.set(HConstants.ZOOKEEPER_QUORUM, "localhost:2181")
    conn = ConnectionFactory.createConnection(conf)
  }

  override def invoke(value: Raw, context: SinkFunction.Context[_]): Unit = {
    println(value)
    val t: Table = conn.getTable(TableName.valueOf(tableName))

    val put: Put = new Put(Bytes.toBytes(value.date_time))
    put.addColumn(Bytes.toBytes(family), Bytes.toBytes("name"), Bytes.toBytes(value.date_time.toString))
    put.addColumn(Bytes.toBytes(family), Bytes.toBytes("id_no"), Bytes.toBytes(value.keywordList.toString))
    t.put(put)
    t.close()
  }

  override def close(): Unit = {
    super.close()
    conn.close()
  }
}