package data_stream.watermark
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

object watermark_orderness {

   case class game_gateway_data(user_id: String, step_num: String, gateway_time: Long, gateway_name: String)

   def main(args: Array[String]): Unit = {
     val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

     env.setParallelism(1)

     env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

     val socketStream: DataStream[String] = env.socketTextStream("localhost",8888)

     val gameGatewayStream: DataStream[game_gateway_data] = socketStream.map((line: String) => {
       val array_data: Array[String] = line.split(",")
       game_gateway_data(array_data(0), array_data(1), array_data(2).toLong, array_data(3))
     })


     gameGatewayStream.print("test")

     env.execute("test case2")

   }
}
