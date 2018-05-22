import java.time.Instant

package object twitter_status_monitor {
  case class Tweet(id: Long, created_at: Instant, archived: Boolean = false) // hashtags: Seq[String],
  case class TweetStatistic(id: Long, favorite_count: Int, retweet_count: Int, user: User, time_stamp: Instant = Instant.now())
  case class Token(bearer: String, accessToken: String)
  case class User(id: Int, followers_count: Int)
}
