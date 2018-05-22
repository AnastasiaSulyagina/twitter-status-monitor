package twitter_status_monitor.server.controllers

import akka.http.scaladsl.server.Route
import twitter_status_monitor.{Tweet, TweetStatistic}
import twitter_status_monitor.server.Controller
import twitter_status_monitor.services.TweetService

/**
  * Created by anastasia.sulyagina
  */
object TweetController {

  case class AddTweetRequest(url: String)
  case class AddTweetResponse(tweet: Option[Tweet])

  case class GetTweetStatsRequest(url: String)
  case class GetTweetStatsResponse(tweetStats: Seq[TweetStatistic])
}

class TweetController(tweetService: TweetService) extends Controller {
  override def routes: Route = getTweetStatsRoute ~ addTweetRoute

  private val addTweetRoute = (path("tweets/watch") & post & entity(as[TweetController.AddTweetRequest])) { request =>
    tweetService.getNewTweet(request.url).map(TweetController.AddTweetResponse)
  }

  private val getTweetStatsRoute = (path("tweets/stats") & post & entity(as[TweetController.GetTweetStatsRequest])) { request =>
    tweetService.getAllTweetStats(request.url).map(TweetController.GetTweetStatsResponse)
  }
}
