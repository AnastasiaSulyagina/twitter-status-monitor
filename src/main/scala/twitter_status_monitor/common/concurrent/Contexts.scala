package twitter_status_monitor.common.concurrent

import java.util.concurrent.ForkJoinPool

import scala.concurrent.ExecutionContext

object Contexts {
  val app: ExecutionContext = ExecutionContext.fromExecutor(new ForkJoinPool(16))
}

trait ApplicationContext {
  implicit val context: ExecutionContext = Contexts.app
}
