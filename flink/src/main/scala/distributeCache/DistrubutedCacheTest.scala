package distributeCache

import java.io.File
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import scala.collection.mutable
import scala.io.{BufferedSource, Source}

object DistrubutedCacheTest {
  def main(args: Array[String]): Unit = {
    //1. Environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    //2. Read the resources on hdfs and set them in the distributed cache
    env.registerCachedFile("hdfs://node01:9000/flink/cache/gender.txt","hdfsGenderInfo")

    //3. Read the student information sent by the socket in real time, calculate it, and output the result
    //(101,"jackson",1,"Shanghai")
    env.socketTextStream("node01",8888)
      .filter((_: String).trim.nonEmpty)
      .map(new RichMapFunction[String,(Int,String,Char,String)] {

        //Used to store student information read from the distributed cache
        val map:mutable.Map[Int,Char]= mutable.HashMap()
        var bs: BufferedSource = _

        override def open(parameters: Configuration): Unit = {
          //1. Read the data stored in the distributed cache
          var file:File = getRuntimeContext.getDistributedCache.getFile("hdfsGenderInfo")

          //2. Encapsulate the read information into a map instance for storage
          bs = Source.fromFile(file)
          val lst: List[String] = bs.getLines().toList
          for(perLine <-lst){
            val arr: Array[String] = perLine.split(",")
            val genderFlg: Int = arr(0).trim.toInt
            val genderName: Char = arr(1).trim.toCharArray()(0)
            map.put(genderFlg,genderName)
          }
        }

        override def map(perStudentInfo: String): (Int, String, Char, String) = {
          //Get student details
          val arr: Array[String] = perStudentInfo.split(",")
          val id: Int = arr(0).trim.toInt
          val name: String = arr(1).trim
          val genderFlg: Int = arr(2).trim.toInt
          val address: String = arr(3).trim
          //According to the data in the distributed cache stored in the container Map, replace the gender identifier in the student information with the real gender
          var genderName: Char = map.getOrElse(genderFlg, 'x')
          (id, name, genderName, address)
        }

        override def close(): Unit = {
          if(bs != null){
            bs.close()
          }
        }
      }).print("The information completed by the student is ->")

    //4. Start
    env.execute(this.getClass.getSimpleName)
  }
}
