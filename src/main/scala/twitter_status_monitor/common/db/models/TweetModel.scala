package twitter_status_monitor.common.db.models

import java.time.Instant

import twitter_status_monitor.common.db.CustomMapping
import twitter_status_monitor.{Tweet, User}

/**
  *  Статистика содержит в себе:
  *    - Количество лайков
  *    - Количество комментариев
  *    - Количество ретвиттов
  *    - Хештеги (неизменяемое)
  *    - Количество подписчиков
  *    - Время публикации твитта (неизменяемое)
  */
trait TweetModel extends DatabaseModel with CustomMapping {
  import profile.api._

  class Tweets(tag: Tag) extends Table[Tweet](tag, "tweet") {
    def id = column[String]("id", O.PrimaryKey)
    def created_at = column[Instant]("created_at")
    def archived = column[Boolean]("archived")

    override def * = (id, created_at, archived) <> (Tweet.tupled, Tweet.unapply)
  }

  val tweets = TableQuery[Tweets]
}
