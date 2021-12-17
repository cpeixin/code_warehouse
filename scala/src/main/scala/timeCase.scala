import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import scala.math.{exp, log}

/**
 * @author Congpeixin
 * @date 2021/11/17 2:53 下午
 * @version 1.0
 * @describe
 */
object timeCase {
  def main(args: Array[String]): Unit = {
//    val N0 = 1
//    val e: Double = exp(1)
//    val λ: Double = -log(0.5)
//    val day="20211116"
//    val hour="09"
//
//    val NOWTIME = day.substring(0,4)+"-"+day.substring(4,6)+"-"+day.substring(6,8)+" "+hour+":00:00"
//    println(NOWTIME)
//    val days = getDayDiff(NOWTIME, "2021-11-17 09:00:00.0".substring(0, 19))
//    println(days)
//    val N = scala.math.pow(N0 * e, (-λ * days)).formatted("%.2f").toFloat
//    println(N)
//
//
//    println(5.0/0)

    val CreateAt = getCreateAt
    println(CreateAt)
  }

  def getCreateAt: String = {
    var dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH")
    var cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.HOUR, -1)
    var yesterday = dateFormat.format(cal.getTime())
    yesterday
  }

  def getNowTime: String = {
    var now: Date = new Date()
    var dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var date = dateFormat.format(now)
    date
  }
  def getDayDiff(start_time: String, end_Time: String): Float = {
    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val begin: Date = df.parse(start_time)
    val end: Date = df.parse(end_Time)
    val between: Long = (end.getTime() - begin.getTime()) / 1000 //转化成秒
    val day: Float = ((between.toFloat / 3600 / 12) * 10).toInt / 10F
    day
  }

  def getPartition: String = {
    var dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH")
    var cal: Calendar = Calendar.getInstance()
    cal.add(Calendar.HOUR, -1)
    var yesterday = dateFormat.format(cal.getTime())
    yesterday
  }

}
