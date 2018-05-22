package twitter_status_monitor.modules

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.softwaremill.macwire._
import twitter_status_monitor.http.HttpController
import twitter_status_monitor.server.Controller
import twitter_status_monitor.server.controllers.TweetController
import twitter_status_monitor.modules.AkkaModule

trait ControllersModule { _: ServiceModule with AkkaModule =>
  lazy val tweetController: TweetController = wire[TweetController]

  lazy val routes: Route = wireSet[Controller].foldLeft[Route](reject)(_ ~ _.routes)
}
