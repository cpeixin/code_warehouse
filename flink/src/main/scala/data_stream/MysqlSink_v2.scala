package data_stream

import java.sql.{Connection, DriverManager, PreparedStatement, SQLException}

import data_stream.datastream_2_mysql.Raw
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.slf4j.{Logger, LoggerFactory}

class MysqlSink_v2 extends RichSinkFunction[Raw] {

  val logger: Logger = LoggerFactory.getLogger("MysqlSink")
  var conn: Connection = _
  var ps: PreparedStatement = _
  val jdbcUrl = "jdbc:mysql://localhost:3306?useSSL=false&allowPublicKeyRetrieval=true"
  val username = "root"
  val password = "cpx726175"
  val driverName = "com.mysql.jdbc.Driver"

  override def open(parameters: Configuration): Unit = {

    Class.forName(driverName)
    try {
      Class.forName(driverName)
      conn = DriverManager.getConnection(jdbcUrl, username, password)

      // close auto commit
      conn.setAutoCommit(false)
    } catch {
      case e@(_: ClassNotFoundException | _: SQLException) =>
        logger.error("init mysql error")
        e.printStackTrace()
        System.exit(-1);
    }
  }

  /**
    * 吞吐量不够话，可以将数据暂存在状态中，批量提交的方式提高吞吐量（如果oom，可能就是数据量太大，资源没有及时释放导致的）
    * @param raw
    * @param context
    */
  override def invoke(raw: Raw, context: SinkFunction.Context[_]): Unit = {
    println("data : " + raw.toString)
    ps = conn.prepareStatement("insert into test.t_weibo_keyword(date_time,keywordList) values(?,?)")
    ps.setString(1, raw.date_time)
    ps.setString(2, raw.keywordList)
    ps.execute()
    conn.commit()
  }
  override def close(): Unit = {
    if (conn != null){
      conn.commit()
      conn.close()
    }
  }
}