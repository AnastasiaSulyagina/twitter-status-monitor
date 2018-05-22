package workshop.services

import com.typesafe.config.ConfigFactory
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import liquibase.{Contexts, LabelExpression, Liquibase}
import org.scalatest.{BeforeAndAfterAll, Suite}
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import slick.jdbc
import slick.jdbc.JdbcBackend

trait DatabaseTest extends BeforeAndAfterAll { _: Suite =>
  val postgres = new PostgreSQLContainer()
  postgres.start()

  val database: JdbcBackend.Database = {
    val config = ConfigFactory.parseString(
      s"""
         | db {
         |  connectionPool = disabled
         |
         |   properties {
         |     url: "${postgres.getJdbcUrl}"
         |     user: "${postgres.getUsername}"
         |     password: "${postgres.getPassword}"
         |   }
         | }
    """.stripMargin).withFallback(ConfigFactory.load())

    println(config.getConfig("db"))

    val db = JdbcBackend.Database.forConfig("db", config)
    runMigrations(db)
    db
  }


  override protected def afterAll(): Unit = {
    database.shutdown
    postgres.stop()

    super.afterAll()
  }

  private def runMigrations(db: jdbc.JdbcBackend.Database): Unit = {
    val s = db.createSession()
    val c = s.conn
    val liquidDb = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c))
    liquidDb.setDefaultSchemaName("public")
    liquidDb.setLiquibaseSchemaName("public")

    new Liquibase("migrations/changelog.xml", new ClassLoaderResourceAccessor(), liquidDb).update(new Contexts(), new LabelExpression())
    new Liquibase("test-migrations/test-changelog.xml", new ClassLoaderResourceAccessor(), liquidDb).update(new Contexts(), new LabelExpression())

    s.close()
  }
}
