ThisBuild / scalaVersion := "2.13.9"

ThisBuild / version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """wardrobe-mgmt""",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,
      "com.typesafe.play" %% "play-json" % "2.9.3",
      jdbc,
      "com.typesafe.play" %% "play-slick" % "5.0.2",
      "com.typesafe.play" %% "play-slick-evolutions" % "5.0.2" % Test,
      "org.postgresql" % "postgresql" % "42.2.5",
      "com.github.tototoshi" %% "scala-csv" % "1.3.10"
    ),
    resolvers += Resolver.typesafeRepo("releases")
  )
