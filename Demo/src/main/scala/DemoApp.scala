//reference: 
// https://github.com/luvgupta008/ScreamingTwitter/blob/master/src/main/scala/com/spark/streaming/TwitterTransmitter.scala
// https://github.com/datastax/spark-cassandra-connector/blob/master/spark-cassandra-connector-demos/twitter-streaming/src/main/scala/com/datastax/spark/connector/demo/TwitterStreamingTopicsByInterval.scala
// http://spark.apache.org/docs/latest/streaming-programming-guide.html#checkpointing

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.twitter._
import com.datastax.spark.connector._
import com.datastax.spark.connector.streaming._

object DemoApp {
  def main(args: Array[String]) {

  	val consumerKey       = "YOUR_CONSUMERKEY"
	val consumerSecret    = "YOUR_CONSUMERSECRET"
	val accessToken       = "YOUR_ACCESSTOKEN"
	val accessTokenSecret = "YOUR_ACCESSTOKENSECRET"

    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
  	
  	//initialize a SparkConf
    val sparkConf = new SparkConf().setAppName("Demo Application")
	sparkConf.set("spark.cassandra.connection.host", "IP_ADDRESS_OF_CASSANDRA") 
    sparkConf.set("spark.cassandra.connection.keep_alive_ms", "60000")    
        
    println("Initializing Twitter stream...")    
    val ssc = new StreamingContext(sparkConf, Seconds(30))

    // Create a DStream the gets streaming data from Twitter      
    val stream = TwitterUtils.createStream(ssc, None, Nil)   
    stream.count().print()
    
    //extract tweet id, tweet text and tweet time
    stream.map(status => {        
        val tid = status.getId
        val tweet = status.getText        
        val createdAt = status.getCreatedAt.getTime()
        (tid, tweet, createdAt)
    }).saveToCassandra("test", "twitter", SomeColumns("tid", "tweet", "createdat"))
        
    ssc.start()     
    ssc.awaitTermination()
  }
}
