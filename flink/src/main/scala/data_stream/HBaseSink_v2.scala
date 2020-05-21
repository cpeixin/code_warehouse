package data_stream

import java.security.MessageDigest
import java.util
import java.util.ArrayList

import data_stream.datastream_2_hbase.Raw
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.hadoop.hbase.{HBaseConfiguration, HConstants, TableName}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes

import scala.util.Try

class HBaseSink_v2 extends RichSinkFunction[Raw]{

  var conn: Connection = null
  var table: Table = null
  val scan: Scan = null

  /**
    * 建立HBase连接
    * @param parameters
    */
  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    val tableName: TableName = TableName.valueOf("t_weibo_keyword")
    val config:org.apache.hadoop.conf.Configuration = HBaseConfiguration.create
    config.set(HConstants.ZOOKEEPER_QUORUM, "localhost")
    config.set(HConstants.ZOOKEEPER_CLIENT_PORT, "2181")
//    config.setInt(HConstants.HBASE_CLIENT_OPERATION_TIMEOUT, 30000)
//    config.setInt(HConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD, 30000)
    conn = ConnectionFactory.createConnection(config)
    table = conn.getTable(tableName)
  }

  /**
    * 处理获取的hbase数据
    * @param value
    * @param context
    */
//  override def invoke(value: Raw, context: SinkFunction.Context[_]): Unit = {
//    print(value)
//    val tableName: TableName = TableName.valueOf("t_weibo_keyword")
//    val cf1 = "cf1"
//    val put: Put = new Put(Bytes.toBytes(MD5Encode(value.date_time)))
//    put.addColumn(Bytes.toBytes(cf1), Bytes.toBytes("date_time"), Bytes.toBytes(value.date_time))
//    put.addColumn(Bytes.toBytes(cf1), Bytes.toBytes("keywordList"), Bytes.toBytes(value.keywordList))
//    val putList: util.ArrayList[Put] = new util.ArrayList[Put]
//    putList.add(put)
//    //设置缓存1m，当达到1m时数据会自动刷到hbase
//    val params: BufferedMutatorParams = new BufferedMutatorParams(tableName)
//    //设置缓存的大小
//    params.writeBufferSize(1024 * 1024)
//    val mutator: BufferedMutator = conn.getBufferedMutator(params)
//    mutator.mutate(putList)
//    mutator.flush()
//    putList.clear()
//  }
  override def invoke(value: Raw, context: SinkFunction.Context[_]): Unit = {
    println(value.toString)
    val tableName: TableName = TableName.valueOf("t_weibo_keyword")
    val cf1 = "cf1"
    val put: Put = new Put(Bytes.toBytes(MD5Encode(value.date_time+scala.util.Random.nextInt(100).toString)))
    put.addColumn(Bytes.toBytes(cf1), Bytes.toBytes("date_time"), Bytes.toBytes(value.date_time))
    put.addColumn(Bytes.toBytes(cf1), Bytes.toBytes("keywordList"), Bytes.toBytes(value.keywordList))
//    val putList: util.ArrayList[Put] = new util.ArrayList[Put]
//    putList.add(put)
//    //设置缓存1m，当达到1m时数据会自动刷到hbase
//    val params: BufferedMutatorParams = new BufferedMutatorParams(tableName)
//    //设置缓存的大小
//    params.writeBufferSize(1024 * 1024)
//    val mutator: BufferedMutator = conn.getBufferedMutator(params)
//    mutator.mutate(putList)
//    mutator.flush()
//    putList.clear()

    Try(table.put(put)).getOrElse(table.close())
}


  /**
    * 关闭
    */
  override def close(): Unit = {
    if (table != null) table.close()
    if (conn != null) conn.close()
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