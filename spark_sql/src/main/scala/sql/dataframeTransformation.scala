package sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ListBuffer
case class userinfo(uuid:String, app_1:String, app_2:String, app_3:String, app_4:String,
                    app_5:String, app_6:String, app_7:String, app_8:String, app_9:String,
                    app_10:String, app_11:String,app_12:String)
object dataframeTransformation {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf()
      .setAppName("hive test")
      .setMaster("local[2]")

    val sc = new SparkContext(conf)

    val array_other_apps = Array("K球", "亚博", "188金宝博")
    val app_list = List("微信球","QQ球","今日球","你好球","懂车球","网易球","K球","亚博球","哈哈球","188金宝博","美团球","滴滴球")

    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    // this is used to implicitly convert an RDD to a DataFrame.
    import sqlContext.implicits._

    val user_app_DF: DataFrame = Seq(
      ("ijefnakjsbndfkai@@DQ#@FD", "360,K球,小红书,QQ"),
      ("@C$R@RGV#$%V#$%VCXZXDFWT#C$#C$", "PP体育,微信,腾讯视频"),
      ("W$X@#$CR@#X$C#CXCXCXQWEFTB^N&M*N", ""),
      ("DFV￥VV￥T#CQWEFDFGHJNT*UB$", "极客时间,网易云音乐,亚博球,188金宝博")
    ).toDF("uuid", "apps")


//    val res: Dataset[(String, Array[String])] = user_app_DF.map((x: Row) => {
//      val uuid: String = x.getAs[String]("uuid")
//      val apps: String = x.getAs[String]("apps")
//      val app_array: Array[String] = apps.split(',')
//      val res_apps: Array[String] = app_array.intersect(array_other_apps)
//      (uuid, res_apps)
//    })



    //    +--------------------+------------+
    //    |                  _1|          _2|
    //    +--------------------+------------+
    //    |ijefnakjsbndfkai@...|        [K球]|
    //    |@C$R@RGV#$%V#$%VC...|          []|
    //    |W$X@#$CR@#X$C#CXC...|          []|
    //    |DFV￥VV￥T#CQWEFDFG...|[亚博, 188金宝博]|
    //    +--------------------+------------+

    val frame: DataFrame = user_app_DF.map((x: Row) => {

      val uuid: String = x.getAs[String]("uuid")
      val apps: String = x.getAs[String]("apps")
      val app_array: Array[String] = apps.split(',')
      val res_apps: Array[String] = app_array.intersect(array_other_apps)
      var flag_list = new ListBuffer[String]()
      for (app <- app_list) {
        if (res_apps.contains(app)) {
          flag_list += "yes"
        }
        else {
          flag_list += "no"
        }
      }
      flag_list = uuid +: flag_list
      userinfo(flag_list.head, flag_list(1), flag_list(2), flag_list(3), flag_list(4), flag_list(5), flag_list(6), flag_list(7), flag_list(8), flag_list(9), flag_list(10), flag_list(11), flag_list(12))
    }).toDF("uuid", "微信球", "QQ球", "今日球", "你好球", "懂车球", "网易球", "K球", "亚博球", "哈哈球", "188金宝博", "美团球", "滴滴球")
    frame.show()





//    +--------------------+---+---+---+---+---+---+---+---+---+------+---+---+
//    |                uuid|微信球|QQ球|今日球|你好球|懂车球|网易球| K球|亚博球|哈哈球|188金宝博|美团球|滴滴球|
//    +--------------------+---+---+---+---+---+---+---+---+---+------+---+---+
//    |ijefnakjsbndfkai@...| no| no| no| no| no| no|yes| no| no|    no| no| no|
//    |@C$R@RGV#$%V#$%VC...| no| no| no| no| no| no| no| no| no|    no| no| no|
//    |W$X@#$CR@#X$C#CXC...| no| no| no| no| no| no| no| no| no|    no| no| no|
//    |DFV￥VV￥T#CQWEFDFG...| no| no| no| no| no| no| no| no| no|   yes| no| no|
//    +--------------------+---+---+---+---+---+---+---+---+---+------+---+---+

  }






}
