import com.holdenkarau.spark.testing.StreamingSuiteBase
import org.apache.spark.streaming.dstream.DStream
import org.scalatest.FunSuite
import streaming.streaming_window.set_word_num

class Top5_StreamingTest extends FunSuite with StreamingSuiteBase {

  test("really simple transformation") {
    val input = List(List("路人甲,路人乙,路人甲,路人乙,路人甲,路人乙,路人甲," +
      "路人丙,路人甲,路人丙,路人乙,路人丙,路人甲,路人丙,路人乙,路人丁,路人丁" +
      ",路人丁,路人戊,路人戊,路人戊,路人己"))
    val expected = List(List(("路人甲", 6), ("路人乙", 5), ("路人丙", 4), ("路人丁", 3), ("路人戊", 3)))

    testOperation[String, (String, Int)](input, set_word_num(_), expected, ordered = false)
  }

  // This is the sample operation we are testing
  def tokenize(f: DStream[String]): DStream[String] = {
    f.flatMap(_.split(" "))
  }

}