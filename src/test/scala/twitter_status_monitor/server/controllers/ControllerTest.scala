package twitter_status_monitor.server.controllers

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpecLike, Matchers}
import twitter_status_monitor.server.json.{ApplicationProtocol, TethysSupport}

trait ControllerTest extends FlatSpecLike with ScalatestRouteTest with Matchers with TethysSupport with ApplicationProtocol with MockFactory
