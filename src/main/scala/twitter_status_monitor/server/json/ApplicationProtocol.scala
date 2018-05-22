package twitter_status_monitor.server.json

import java.time.Instant

import tethys._
import tethys.derivation.semiauto._
import twitter_status_monitor.server.controllers.TweetController.{AddTweetRequest, AddTweetResponse, GetTweetStatsRequest, GetTweetStatsResponse}
import twitter_status_monitor.{Tweet, TweetStatistic, User}

trait ApplicationProtocol {
  implicit val userReader: JsonReader[User] = jsonReader[User]
  implicit val userWriter: JsonWriter[User] = jsonWriter[User]

  implicit val instantJsonReader: JsonReader[Instant] = JsonReader.stringReader.map(Instant.parse)
  implicit val instantJsonWriter: JsonWriter[Instant] = JsonWriter.stringWriter.contramap(_.toString)

  implicit val tweetJsonReader: JsonReader[Tweet] = jsonReader[Tweet]
  implicit val tweetJsonWriter: JsonWriter[Tweet] = jsonWriter[Tweet]

  implicit val tweetStatisticJsonReader: JsonReader[TweetStatistic] = jsonReader[TweetStatistic]
  implicit val tweetStatisticJsonWriter: JsonWriter[TweetStatistic] = jsonWriter[TweetStatistic]

  // TweetController

  implicit val createUserRequestJsonReader: JsonReader[AddTweetRequest] = jsonReader[AddTweetRequest]
  implicit val createUserRequestJsonWriter: JsonWriter[AddTweetResponse] = jsonWriter[AddTweetResponse]

  implicit val createUserResponseJsonReader: JsonReader[GetTweetStatsRequest] = jsonReader[GetTweetStatsRequest]
  implicit val createUserResponseJsonWriter: JsonWriter[GetTweetStatsResponse] = jsonWriter[GetTweetStatsResponse]

}
