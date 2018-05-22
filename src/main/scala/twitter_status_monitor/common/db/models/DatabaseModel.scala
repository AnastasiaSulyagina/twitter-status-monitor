package twitter_status_monitor.common.db.models

import slick.jdbc.JdbcProfile

trait DatabaseModel {
  val profile: JdbcProfile
}
