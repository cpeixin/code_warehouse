//package rdd
//import java.lang
//import java.sql.{Connection, DriverManager, PreparedStatement}
//
//import org.apache.hadoop.hbase.client.Put
//import org.apache.hadoop.hbase.io.ImmutableBytesWritable
//import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableOutputFormat}
//import org.apache.hadoop.hbase.client.Result
//import org.apache.hadoop.hbase.util.Bytes
//import org.apache.hadoop.mapreduce.Job
//import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.hbase.HBaseConfiguration
//import org.apache.hadoop.mapred.JobConf
//import org.apache.spark.rdd.RDD
//import org.apache.spark.{SparkConf, SparkContext}
//
//object process_json {
//  def main(args: Array[String]): Unit={
//    val sparkConf: SparkConf = new SparkConf()
//      .setAppName("process_json")
//
//    """
//    data_demo:
//    {"user_name":"brent","customer_id":12031602,"age":22,"birthday":"1993-04-05","deposit_amount":3000,"last_login_time":"2017-03-10 14:55:22"}
//    {"user_name":"haylee","customer_id":12031603,"age":23,"birthday":"1992-08-10","deposit_amount":4000.56,"last_login_time":"2017-03-11 10:55:00"}
//    {"user_name":"vicky","customer_id":12031604,"age":30,"birthday":"2000-03-02","deposit_amount":200.4,"last_login_time":"2017-03-10 09:10:00"}
//    """.stripMargin
//    val sc = new SparkContext(sparkConf)
//    var json_rdd: RDD[String] = sc.textFile("hdfs://localhost:8020/data/user_data.json")
//
//    val result_rdd: RDD[JSONObject] = json_rdd.map((x: String) => {
//
//      val json_item: JSONObject = JSON.parseObject(x)
//      json_item.put("flag", 1)
//      json_item
//    })
//
////    写入hdfs
////    result_rdd.saveAsTextFile("hdfs://localhost:8020/data/result/user_data")
//
////    写入mysql
////    val driverClassName = "com.mysql.jdbc.Driver"
////    val url = "jdbc:mysql://localhost:3306/test?characterEncoding=utf8&useSSL=false"
////    val user = "root"
////    val password = "cpx726175"
////
////    result_rdd.foreachPartition((partition: Iterator[JSONObject]) => {
////      Class.forName(driverClassName)
////      val connection: Connection = DriverManager.getConnection(url, user, password)
////      val sql = "insert into t_user(user_name, customer_id, age, birthday, deposit_amount, last_login_time,flag) values(?,?,?,?,?,?,?)"
////      val statement: PreparedStatement = connection.prepareStatement(sql)
////      try {
////        partition.foreach {
////          json_data: JSONObject => {
////            statement.setString(1, json_data.getString("user_name"))
////            statement.setInt(2, json_data.getInteger("customer_id"))
////            statement.setInt(3, json_data.getInteger("age"))
////            statement.setString(4, json_data.getString("birthday"))
////            statement.setFloat(5, json_data.getFloat("deposit_amount"))
////            statement.setString(6, json_data.getString("last_login_time"))
////            statement.setInt(7, json_data.getInteger("flag"))
////            statement.executeUpdate()
////
////          }
////        }
////      }catch {
////        case e: Exception =>  println(e.printStackTrace())
////      }
////      finally {
////        if(statement!=null) statement.close()
////        if(connection!=null) connection.close()
////      }
////
////
////      connection.close()
////    }
////    )
//
//
//    var resultConf: Configuration = HBaseConfiguration.create()
//    //设置zooKeeper集群地址，也可以通过将hbase-site.xml导入classpath，但是建议在程序里这样设置
//    resultConf.set("hbase.zookeeper.quorum", "localhost")
//    //设置zookeeper连接端口，默认2181
//    resultConf.set("hbase.zookeeper.property.clientPort", "2181")
//    //注意这里是output
//    resultConf.set(TableOutputFormat.OUTPUT_TABLE, "t_user")
//    var job: Job = Job.getInstance(resultConf)
//    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
//    job.setOutputValueClass(classOf[org.apache.hadoop.hbase.client.Result])
//    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
//
//    val hbaseOut: RDD[(ImmutableBytesWritable, Put)] = result_rdd.map((json_data: JSONObject) => {
//      val put = new Put(Bytes.toBytes(json_data.getInteger("customer_id").toString))
//      put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("user_name"), Bytes.toBytes(json_data.getString("user_name")))
//      //直接写入整型会以十六进制存储
//      put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("customer_id"), Bytes.toBytes(json_data.get("customer_id").toString))
//      put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("age"), Bytes.toBytes(json_data.get("age").toString))
//      put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("birthday"), Bytes.toBytes(json_data.getString("birthday")))
//      put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("deposit_amount"), Bytes.toBytes(json_data.get("deposit_amount").toString))
//      put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("last_login_time"), Bytes.toBytes(json_data.getString("last_login_time")))
//      put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("flag"), Bytes.toBytes(json_data.get("flag").toString))
//      (new ImmutableBytesWritable, put)
//    })
//
//    hbaseOut.saveAsNewAPIHadoopDataset(job.getConfiguration)
//
//    sc.stop()
//
//  }
//}
