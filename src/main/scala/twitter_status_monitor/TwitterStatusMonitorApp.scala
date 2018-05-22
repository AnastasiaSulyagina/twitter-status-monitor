package twitter_status_monitor

import akka.http.scaladsl.Http
import org.slf4j.LoggerFactory
import twitter_status_monitor.common.concurrent.ApplicationContext
import twitter_status_monitor.modules._

trait Dependencies extends AkkaModule with DatabaseModule with ServiceModule with ControllersModule with ConfigModule

object TwitterStatusMonitorApp extends App with Dependencies with ApplicationContext {
  val logger = LoggerFactory.getLogger("app")

  val (host, port) = config.getString("http.host") -> config.getInt("http.port")
  val binding = Http().bindAndHandle(routes, host, port)
  logger.info(s"Server started at $host:$port")

  sys.addShutdownHook {
    binding.flatMap(_.unbind()).flatMap(_ => system.terminate()).map(_ => logger.info("Server stopped"))
  }
}