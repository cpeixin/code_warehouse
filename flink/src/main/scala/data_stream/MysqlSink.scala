package data_stream

import java.sql.{Connection, DriverManager, PreparedStatement}

import com.alibaba.fastjson.{JSON, JSONObject}
import data_stream.datastream_2_mysql.Raw
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.logging.log4j.Logger

class MysqlSink(url: String, user: String, pwd: String) extends RichSinkFunction[Raw] {

  var conn: Connection = _

  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    Class.forName("com.mysql.jdbc.Driver")
    conn = DriverManager.getConnection(url, user, pwd)
    conn.setAutoCommit(false)
  }

  override def invoke(value: Raw, context: SinkFunction.Context[_]): Unit = {
    print(value)
    val p: PreparedStatement = conn.prepareStatement("into t_weibo_keyword(date_time,keywordList) values(?,?)")
    p.setString(1, value.date_time)
    p.setString(2, value.keywordList)
    p.execute()
    conn.commit()
  }

  override def close(): Unit = {
    super.close()
    conn.close()
  }

}
