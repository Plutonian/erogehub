ThisBuild / organization := "com.goexp.galgame"
ThisBuild / version      := "1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.8.1"

lazy val commonSettings = Seq(
  scalacOptions += "-encoding",
  scalacOptions += "UTF-8",
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.6.3" % Runtime,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.6"
  )
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.goexp" %% "core" % "1.0.7-SNAPSHOT",
      "com.goexp.db" %% "db_mongo" % "1.0.7-SNAPSHOT", // 注意 %% 表示 scala 版本插值，如果是java包用 %
      "org.mongodb" % "mongo-java-driver" % "3.12.14",
      "com.goexp" %% "pipeline" % "1.0.7-SNAPSHOT",
      "org.jsoup" % "jsoup" % "1.23.2"
    )
    ,
    Compile / mainClass := Some("com.goexp.galgame.data.script.ImporterStart")

  )
  .settings(
    name := "root"
  )
