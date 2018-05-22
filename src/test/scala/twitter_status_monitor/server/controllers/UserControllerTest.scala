//package twitter_status_monitor.server.controllers
//
//import akka.http.scaladsl.model.StatusCodes
//import akka.http.scaladsl.server.Route
//import twitter_status_monitor.Tweet
//import twitter_status_monitor.services.{TweetService}
//
//import scala.concurrent.Future
//
//class UserControllerTest extends ControllerTest {
//  behavior of "UserController"
//
//  trait Setup {
//    val tweetService: TweetService = mock[TweetService]
//    lazy val controller = new TweetController(tweetService)
//    lazy val routes: Route = Route.seal(controller.routes)
//  }
//
//  "POST /users" should "create new user" in new Setup {
//    val login = "test-login"
//    val firstName = Some("John")
//    val lastName = Some("Doe")
//    val userStatus = Some("At work")
//
//    (tweetService.createUser _).expects(login, firstName, lastName, userStatus).returning(Future.successful(User(login, firstName, lastName, userStatus)))
//
//    Post[TweetController.CreateUserRequest]("/users", TweetController.CreateUserRequest(login, firstName, lastName, userStatus)) ~> routes ~> check {
//      status shouldBe StatusCodes.OK
//      entityAs[TweetController.CreateUserResponse] shouldBe TweetController.CreateUserResponse(User(
//        login = login,
//        firstName = firstName,
//        lastName = lastName,
//        status = userStatus
//      ))
//    }
//  }
//
//  "POST /users/{id}/status" should "update user status" in new Setup {
//    val login = "test-login"
//    val firstName = Some("John")
//    val lastName = Some("Doe")
//    val userStatus = Some("At work")
//
//    (userService.changeUserStatus _).expects(login, userStatus).returning(Future.successful(User(login, firstName, lastName, userStatus)))
//
//    Post[UserController.ChangeUserStatusRequest](s"/users/$login/status", UserController.ChangeUserStatusRequest(userStatus)) ~> routes ~> check {
//      status shouldBe StatusCodes.OK
//
//      entityAs[UserController.ChangeUserStatusResponse] shouldBe UserController.ChangeUserStatusResponse(User(
//        login = login,
//        firstName = firstName,
//        lastName = lastName,
//        status = userStatus
//      ))
//    }
//  }
//}