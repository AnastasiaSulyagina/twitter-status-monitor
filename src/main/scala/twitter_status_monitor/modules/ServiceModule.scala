package twitter_status_monitor.modules

import com.softwaremill.macwire._
import twitter_status_monitor.services._

trait ServiceModule { _: DatabaseModule =>
  lazy val tweetService: TweetService = wire[TweetServiceImpl]
}
