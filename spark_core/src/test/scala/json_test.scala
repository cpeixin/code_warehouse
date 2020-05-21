import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.{SparkConf, SparkContext}

object json_test {
  def main(args: Array[String]): Unit= {
    val sparkConf: SparkConf = new SparkConf()
      .setAppName("json_test")
      .setMaster("local")

    """
    data_demo:
    {"user_name":"brent","customer_id":12031602,"age":22,"birthday":"1993-04-05","deposit_amount":3000,"last_login_time":"2017-03-10 14:55:22"}
    {"user_name":"haylee","customer_id":12031603,"age":23,"birthday":"1992-08-10","deposit_amount":4000.56,"last_login_time":"2017-03-11 10:55:00"}
    {"user_name":"vicky","customer_id":12031604,"age":30,"birthday":"2000-03-02","deposit_amount":200.4,"last_login_time":"2017-03-10 09:10:00"}
    """.stripMargin
    val sc = new SparkContext(sparkConf)
    val json_str = "{\"user_name\":\"brent\",\"customer_id\":12031602,\"age\":22,\"birthday\":\"1993-04-05\", " +
      "\"deposit_amount\":3000.66,\"last_login_time\":\"2017-03-10 14:55:22\"}"
    val json_item: JSONObject = JSON.parseObject(json_str)

    val deposit_num = json_item.getDouble("deposit_amount")

    println(deposit_num)

  }
}
