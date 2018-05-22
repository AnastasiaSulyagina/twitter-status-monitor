package twitter_status_monitor.services

import slick.jdbc.{JdbcBackend, JdbcProfile}
import twitter_status_monitor.common.concurrent.ApplicationContext
import twitter_status_monitor.common.db.models.{TweetModel, TweetStatsModel}
import twitter_status_monitor.common.db.{EntityAlreadyExistsException, EntityDoesNotExistException}
import twitter_status_monitor.http.TwitterApi
import twitter_status_monitor.{Tweet, TweetStatistic}

import scala.concurrent.Future

trait TweetService {
  def addTweet(tweet: Tweet): Future[Option[Tweet]]
  def addTweetStatistic(tweetStatistic: TweetStatistic): Future[Option[TweetStatistic]]
  def getTweetFromDB(id: String): Future[Option[Tweet]]
  def getAllTweetStats(id: String): Future[Seq[TweetStatistic]]
  def archiveTweet(id: String): Future[Unit]
  def getNewTweet(url: String): Future[Option[Tweet]]
  def getNewStat(url: String): Future[Option[TweetStatistic]]
  def getAllNewStats(): Future[Unit]
}

class TweetServiceImpl(val profile: JdbcProfile,
                       db: JdbcBackend.Database,
                       twitterApi: TwitterApi)
  extends TweetService with ApplicationContext with TweetModel with TweetStatsModel {

  import profile.api._

  override def addTweet(tweet: Tweet): Future[Option[Tweet]] = {
    db.run {
      tweets.filter(_.id === tweet.id.bind).result.headOption.flatMap {
        case None => tweets += tweet
        case _ => DBIO.failed(new EntityAlreadyExistsException(s"Tweet ${tweet.id} already exists"))
      }.map(_ => Some(tweet))
    }
  }

  override def addTweetStatistic(tweetStatistic: TweetStatistic): Future[Option[TweetStatistic]] = {
    db.run {tweetStats += tweetStatistic}.map(_ => Some(tweetStatistic))
  }

  override def getTweetFromDB(id: String): Future[Option[Tweet]] = {
    db.run {
      tweets.filter(_.id === id.bind).result.headOption
    }
  }

  override def getAllTweetStats(id: String): Future[Seq[TweetStatistic]] = {
    db.run {
      tweetStats.filter(_.tweetId === id.bind).result
    }
  }

  override def getNewTweet(url: String): Future[Option[Tweet]] = {
    twitterApi.getTweet(url.split("/").last).flatMap {
      case Some(tweet) => addTweet(tweet)
      case _ => Future.successful(None)
    }
  }

  override def getNewStat(id: String): Future[Option[TweetStatistic]] = {
    twitterApi.getStats(id).flatMap {
      case Some(stat) => addTweetStatistic(stat)
      case _ => Future.successful(None)
    }
  }

  override def archiveTweet(id: String): Future[Unit] ={
    db.run {
      tweets.filter(_.id === id.bind).map(_.archived).update(true)
    }.map {
      case 1 => ()
      case _ => throw new EntityDoesNotExistException(s"Cannot find tweet $id")
    }
  }

  override def getAllNewStats(): Future[Unit] = {
    db.run {
      tweets.filter(_.archived === false).result
    }.map(_.map{tweet => getNewStat(tweet.id.toString)})
  }
}

