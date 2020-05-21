import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfter, FlatSpec}
import unit_test.WordCount
//引入scalatest建立一个单元测试类，混入特质BeforeAndAfter，在before和after中分别初始化sc和停止sc，
//初始化SparkContext时只需将Master设置为local(local[N],N表示线程)即可，无需本地配置或搭建集群，

class WordCountTests extends FlatSpec with BeforeAndAfter{
  val master="local" //sparkcontext的运行master 
  var sc:SparkContext=_
  "wordcount_class" should "map word ,1"  in{
    //其中参数为rdd或者dataframe可以通过通过简单的手动构造即可
    val seq=Seq("Brent","HayLee","Henry")
    val rdd=sc.parallelize(seq)
    val wordCounts=WordCount.count(rdd)
    wordCounts.map(p=>{
      p._1 match {
        case "Brent"=>
          assert(p._2==1)// 断言
        case "HayLee"=>
          assert(p._2==1)
        case "Henry"=>
          assert(p._2==1)
        case _=>
          None
      }
    }).foreach(_=>())
  }
  //这里before和after中分别进行sparkcontext的初始化和结束，如果是SQLContext也可以在这里面初始化
  before{
    val conf=new SparkConf()
      .setAppName("test").setMaster(master)
    sc=new SparkContext(conf)
  }

  after{
    if(sc!=null){
      sc.stop()
    }
  }
}