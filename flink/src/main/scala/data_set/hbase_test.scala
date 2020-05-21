package data_set

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.util.Collector
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{Put, Table}
import org.apache.hadoop.hbase.util.Bytes
import utils.HBaseUtil

import scala.collection.mutable.ArrayBuffer

object hbase_test {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    import org.apache.flink.api.scala._
    val list = ArrayBuffer[String]()
    list.append("my-key-1")
    val text = env.fromCollection(list)
    text.map((_, 1)).process(new ProcessFunction[(String, Int), String] {
      var table: Table = _

      override def open(parameters: Configuration): Unit = {
        table = HBaseUtil.getHBaseConn.getTable(TableName.valueOf("t_weibo_keyword_2"))
      }

      override def processElement(value: (String, Int), ctx: ProcessFunction[(String, Int), String]#Context, out: Collector[String]): Unit = {

        //写入
        val put = new Put(Bytes.toBytes("shx_" + value._2))
        put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("name"), Bytes.toBytes("lisi"))
        table.put(put)
      }

      override def close(): Unit = {
        table.close()
      }
    })


    env.execute("Flink")
}}
