package twitter_status_monitor.common.db

sealed abstract class DatabaseException(message: String, cause: Option[Throwable] = None) extends Exception(message, cause.orNull)
class EntityAlreadyExistsException(message: String) extends Exception(message)
class EntityDoesNotExistException(message: String) extends Exception(message)