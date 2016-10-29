import java.util.Properties
import java.time

import com.mongodb.spark.{MongoConnector, MongoSpark}
import com.mongodb.spark.config.{ReadConfig, WriteConfig}
import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation

import scala.collection.JavaConversions._
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import edu.stanford.nlp.util.CoreMap
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.joda.time.DateTime

/**
  * Created by Rui Yang on 30/09/2016.
  *
  * Calculate average sentiment score of tweets collected.
  */
object Main {

  private val pipeline: StanfordCoreNLP = {
    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, parse, sentiment")
    val pipeline = new StanfordCoreNLP(props)
    pipeline
  }

  def sentimentAnalysis(text: String): Int = {
    val annotation: Annotation = pipeline.process(text)
    var averageSentiScore = 0.0f
    for(sentence: CoreMap <- annotation.get(classOf[CoreAnnotations.SentencesAnnotation])) {
      val tree = sentence.get(classOf[SentimentCoreAnnotations.SentimentAnnotatedTree])
      val sentiment = RNNCoreAnnotations.getPredictedClass(tree)
      val partText = sentence.toString()
      averageSentiScore += sentiment * partText.length()
    }
    averageSentiScore /= text.length()
    if (averageSentiScore > 2) {
      return 1
    } else if (averageSentiScore == 2) {
      return 0
    } else if (averageSentiScore < 2) {
      return -1
    }
    0
  }

  def purifyTweet(tweet: String): String = {
    tweet.replaceAll("https?://\\S*","")
  }

  def main(args: Array[String]): Unit = {
    if(args.length < 1) {
      println("args: mongodb://xxxx")
      // "mongodb://localhost:27017/final_project.tweets"
      System.exit(1);
    }
    val date = DateTime.now()
    val mongoURI = args(0)
    val spark = SparkSession
      .builder
      .appName("Tweet" + date)
      .getOrCreate()
    val sc = spark.sparkContext
    val readConfig = ReadConfig(Map("uri" -> mongoURI))
    val sumCount = MongoSpark.load(sc, readConfig)
      .map(x => x.get("text").asInstanceOf[String])
      .map(x => purifyTweet(x))
      .map(x => sentimentAnalysis(x))
      .aggregate((0,0))(
        (acc, value) => (acc._1 + value, acc._2 + 1),
        (acc1, acc2) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
      )
    println("avg=" + sumCount._1 / sumCount._2.toDouble)
    spark.stop()
  }

}
