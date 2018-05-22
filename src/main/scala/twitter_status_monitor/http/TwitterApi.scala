package twitter_status_monitor.http


import akka.http.javadsl.model.headers.Authorization
import akka.http.scaladsl.model.headers.{Authorization, RawHeader}

import scala.concurrent.{Await, ExecutionContext, Future}
import akka.http.scaladsl.model.{HttpEntity, HttpMethods, HttpRequest, headers}
import twitter_status_monitor.{Token, Tweet, TweetStatistic}
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.concurrent.duration.Duration

trait TwitterApi {
  def auth(): Future[String]

  def getTweet(id: String): Future[Option[Tweet]]

  def getStats(id: String): Future[Option[TweetStatistic]]
}

class RestTradingApi(httpClient: HttpClient)
                    (implicit ec: ExecutionContext)
  extends TwitterApi {

  val token = Await.result(auth().map(_.get.accessToken), Duration.Inf)

  override def auth(): Future[Option[Token]] = {
    val key = "4MbUdmgHZNgj6xUP80WL0TdI6"
    val secret = "gh9fSgNoGmOm6OgrlUQGTCXSbYW9l1iDwBz46Ps3Vg7DYlWoPL"

    val request: HttpRequest = HttpRequest(
      method = HttpMethods.POST,
      uri = s"https://api.twitter.com/oauth2/token",
      entity = HttpEntity("grant_type=client_credentials")
    ).withHeaders(
      RawHeader("Authorization", key + ":" + secret),
      RawHeader("Content-Type", "application/x-www-form-urlencoded")
    )

    httpClient.send(request).map { response =>
      parse(response).extract[Option[Token]]
    }.recover {
      case _ =>
        None
    }
  }

  override def getTweet(id: String): Future[Option[Tweet]] = {
    val request: HttpRequest = HttpRequest(
      method = HttpMethods.GET,
      uri = s"https://api.twitter.com/1.1/statuses/show.json?id=$id"
    ).withHeaders(RawHeader("Authorization", token))

    httpClient.send(request).map { response =>
      parse(response).extract[Option[Tweet]]
    }.recover {
      case _ =>
        None
    }
  }

  override def getStats(id: String): Future[Option[TweetStatistic]] = {
    val request: HttpRequest = HttpRequest(
      method = HttpMethods.GET,
      uri = s"https://api.twitter.com/1.1/statuses/show.json?id=$id"
    ).withHeaders(RawHeader("Authorization", token))

    httpClient.send(request).map { response =>
      parse(response).extract[Option[TweetStatistic]]
    }.recover {
      case _ =>
        None
    }
  }
}
