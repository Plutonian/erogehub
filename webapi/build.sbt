//crossScalaVersions := Seq("2.13.16", "3.8.1")

//scalaVersion := crossScalaVersions.value.head

//    javacOptions ++= Seq("-source", "11", "-target", "11"),
// Play 版本相关依赖
libraryDependencies ++= Seq(
  guice,
  "com.goexp" %% "core" % "1.0.7-SNAPSHOT",
  "com.goexp.db" %% "db_mongo" % "1.0.7-SNAPSHOT",
  "org.mongodb" % "mongo-java-driver" % "3.12.14",
  "com.h2database" % "h2" % "2.4.240",
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test,
)


scalacOptions ++= Seq(
  "-feature",
//  "-Werror"
)


// 端口和 JVM 参数可以用 `javaOptions` 配置，类似 Gradle 的 defaultJvmOpts
javaOptions ++= Seq(
  "-Dhttp.port=9000",
  "-Dpidfile.path=/dev/null"
)

// 启用 routes 生成器（Play 默认已支持 injected routes）
// 这个一般默认开启，如果需要自定义，可以配置
//    playRoutesOptions := playRoutesOptions.value.withInjectedRouter(true)