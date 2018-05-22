name := "twitter-status-monitor"

version := "0.1"

scalaVersion := "2.12.6"

val ItAndTest = "it,test"

lazy val app = (project in file("."))
  .configs(IntegrationTest)
  .enablePlugins(
    sbtdocker.DockerPlugin,
    JavaAppPackaging,
  )
  .settings(Defaults.itSettings)
  .settings(
    libraryDependencies ++= Seq(
      // Macwire
      "com.softwaremill.macwire" %% "macros" % "2.3.0",

      // Akka
      "com.typesafe.akka" %% "akka-http" % "10.1.1",
      "com.typesafe.akka" %% "akka-actor" % "2.5.12",
      "com.typesafe.akka" %% "akka-stream" % "2.5.12",
      "org.json4s" %% "json4s-jackson" % "3.5.3",
      "com.tethys-json" %% "tethys" % "0.6.3",

      // Slick
      "com.typesafe.slick" %% "slick" % "3.2.3",
      "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",
      "org.liquibase" % "liquibase-core" % "3.5.3",
      "com.mattbertolini" % "liquibase-slf4j" % "2.0.0",

      // Logging
      "org.slf4j" % "slf4j-api" % "1.7.25",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",

      // TestContainers
      "org.testcontainers" % "testcontainers" % "1.7.2" % ItAndTest,
      "org.testcontainers" % "postgresql" % "1.7.2" % ItAndTest,

      // Test
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % ItAndTest,
      "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % ItAndTest,
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.12" % ItAndTest,
      "org.scalatest" %% "scalatest" % "3.0.3" % ItAndTest,
      "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % ItAndTest
    )
  )
  .settings(
    dockerfile in docker := {
      val appDir: File = stage.value
      val targetDir = s"/opt/${name.value}"

      new Dockerfile {
        from("java:openjdk-8-jre")
        expose(8080)
        copy(appDir, targetDir)
        workDir(targetDir)
        entryPoint(s"$targetDir/bin/${executableScriptName.value}")
      }
    }
  )