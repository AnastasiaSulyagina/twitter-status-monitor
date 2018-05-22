package twitter_status_monitor.modules

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}

trait AkkaModule {
  implicit val system: ActorSystem = ActorSystem("twitter_status_monitor")
  implicit val materializer: Materializer = ActorMaterializer()
}
