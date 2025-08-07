ThisBuild / organization := "com.goexp.galgame"
ThisBuild / version      := "1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.16"

lazy val commonSettings = Seq(
  scalacOptions += "-encoding",
  scalacOptions += "UTF-8",
  // 通用仓库
  resolvers ++= Seq(
    Resolver.mavenLocal,
    Resolver.mavenCentral,
    "Lightbend Releases" at "https://repo.lightbend.com/lightbend/maven-releases"
  ),
  libraryDependencies ++= Seq(
    "ch.qos.logback" % "logback-classic" % "1.5.18" % Runtime,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
  )
)

lazy val root = (project in file("."))
  .aggregate(common, task,webapi) // 你所有子项目都列这里
  .settings(
    name := "root"
  )

lazy val common = (project in file("common"))
  .settings(commonSettings: _*)
  .settings(

    libraryDependencies ++= Seq(
      "com.goexp" %% "core" % "1.0.5",
      "com.goexp.db" %% "db_mongo" % "1.0.5" % Provided,
      "org.mongodb" % "mongo-java-driver" % "3.12.14" % Provided
    )

  )
  .settings(
    name := "common"
  )

lazy val task = (project in file("task"))
  .dependsOn(common) // 依赖本地子项目 common
  .settings(
    name := "task"
  )
  .settings(
    libraryDependencies ++= Seq(
      "com.goexp.db" %% "db_mongo" % "1.0.5", // 注意 %% 表示 scala 版本插值，如果是java包用 %
      "org.mongodb" % "mongo-java-driver" % "3.12.14",
      "com.goexp" %% "pipeline" % "1.0.5",
      "org.jsoup" % "jsoup" % "1.15.3"
    )
    ,
    Compile / mainClass := Some("com.goexp.galgame.data.script.ImporterStart")

  )



lazy val webapi = (project in file("webapi"))
  .enablePlugins(PlayScala)  // Play Framework 插件
  .dependsOn(common)          // 依赖 common 子项目
  .settings(
    name :="webapi"
  )


//lazy val otherSubproject = (project in file("otherSubproject"))
//  .settings(commonSettings: _*)
//  .settings(
//    name := "otherSubproject"
//  )
