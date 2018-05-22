package twitter_status_monitor.common.db

import java.sql.Timestamp
import java.time.Instant

import slick.jdbc.JdbcProfile

trait CustomMapping {
  val profile: JdbcProfile

  import profile.api._

  implicit val instantColumnType = MappedColumnType.base[Instant, Timestamp](
    { instant => new Timestamp(instant.toEpochMilli) },
    { timestamp => timestamp.toInstant }
  )
}