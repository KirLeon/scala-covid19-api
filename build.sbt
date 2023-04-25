ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"
lazy val root = (project in file("."))
  .settings(
    name := "covidapi",
    idePackagePrefix := Some("com.inno.trainee")
  )

val http4sVersion = "0.23.18"
val http4sBlaze = "0.23.14"
val catsTypeLevelVersion = "2.9.0"
val logbackVersion = "1.4.6"
val jsonJacksonVersion = "4.0.6"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sBlaze,
  "org.http4s" %% "http4s-blaze-client" % http4sBlaze,
  "org.typelevel" %% "cats-core" % catsTypeLevelVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "org.json4s" %% "json4s-jackson" % jsonJacksonVersion
)
