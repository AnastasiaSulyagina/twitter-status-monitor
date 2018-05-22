package twitter_status_monitor.common.db.models

import java.time.Instant

import twitter_status_monitor.{TweetStatistic, User}
import twitter_status_monitor.common.db.CustomMapping

trait TweetStatsModel extends DatabaseModel with CustomMapping {
  import profile.api._

  class TweetStats(tag: Tag) extends Table[TweetStatistic](tag, "tweet_stats") {
    def tweetId = column[Long]("tweet_id")

    def favorite_count = column[Int]("likes")

    def retweet_count = column[Int]("retweets")

    def user_id = column[Int]("user_id")

    def followers = column[Int]("followers")

    def time_stamp = column[Instant]("time_stamp")

    def pk = primaryKey("pk", (tweetId, time_stamp))

    def user = (user_id, followers) <> ( {
      case (user_id, followers) => User(user_id, followers)
    }, { u: User => Some(u.id, u.followers_count) })

    override def * = (tweetId, favorite_count, retweet_count, user, time_stamp) <> (TweetStatistic.tupled, TweetStatistic.unapply _)
  }

  val tweetStats = TableQuery[TweetStats]
}
