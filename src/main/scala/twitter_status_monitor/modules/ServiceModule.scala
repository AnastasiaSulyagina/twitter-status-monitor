package twitter_status_monitor.modules

import com.softwaremill.macwire._
import twitter_status_monitor.http.{AkkaHttpClient, TwitterApiImpl}
import twitter_status_monitor.services._

trait ServiceModule { _: DatabaseModule with AkkaModule =>
  lazy val httpClient = wire[AkkaHttpClient]
  lazy val twitterApi = wire[TwitterApiImpl]
  lazy val tweetService: TweetService = wire[TweetServiceImpl]
}
