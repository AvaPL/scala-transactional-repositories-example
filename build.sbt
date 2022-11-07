ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

lazy val root = (project in file("."))
  .settings(
    name := "scala-transactional-repositories-example",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.3.14",
      "org.tpolecat" %% "doobie-core" % "1.0.0-RC1",
      "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC1"
    )
  )
