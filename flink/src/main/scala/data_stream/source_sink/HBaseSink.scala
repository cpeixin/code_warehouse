package data_stream.source_sink

import java.security.MessageDigest

import data_stream.source_sink.datastream_2_hbase.Raw
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants, TableName}

class HBaseSink() extends RichSinkFunction[Raw] {


  var conn: Connection = _

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    val conf = HBaseConfiguration.create()
    conf.set(HConstants.ZOOKEEPER_QUORUM, "localhost")
    conf.set(HConstants.ZOOKEEPER_CLIENT_PORT, "2181")
    conn = ConnectionFactory.createConnection(conf)
  }

  override def invoke(value: Raw, context: SinkFunction.Context[_]): Unit = {
    println(value)

    val t: Table = conn.getTable(TableName.valueOf("t_weibo_keyword_2"))

    val put: Put = new Put(Bytes.toBytes(value.date_time))
    put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("date_time"), Bytes.toBytes(value.date_time))
    put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("keywordList"), Bytes.toBytes(value.keywordList))
    t.put(put)
    t.close()
  }

  override def close(): Unit = {
    super.close()
    conn.close()
  }

  def MD5Encode(input: String): String = {
    // 指定MD5加密算法
    val md5: MessageDigest = MessageDigest.getInstance("MD5")
    // 对输入数据进行加密,过程是先将字符串中转换成byte数组,然后进行随机哈希
    val encoded: Array[Byte] = md5.digest(input.getBytes)
    // 将加密后的每个字节转化成十六进制，一个字节8位，相当于2个16进制，不足2位的前面补0
    encoded.map("%02x".format(_: Byte)).mkString
  }
}

