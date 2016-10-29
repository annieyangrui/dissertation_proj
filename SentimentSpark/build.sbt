
lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0.1.0",
  scalaVersion := "2.11.8",
  libraryDependencies += "edu.stanford.nlp" % "stanford-parser" % "3.6.0",
  libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0",
  libraryDependencies += "org.mongodb.spark" % "mongo-spark-connector_2.11" % "2.0.0-rc0"
)

lazy val corenlpDependencies = Seq(
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models"
)

lazy val sparkDependencies = Seq(
  "org.apache.spark" % "spark-core_2.11" % "2.0.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.0.0"
)


run in Compile <<= Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run))

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "sbt-example",
    libraryDependencies ++= corenlpDependencies.map(_ % "provided"),
    libraryDependencies ++= sparkDependencies.map(_ % "provided")
  )
lazy val mainRunner = project.in(file("mainRunner")).dependsOn(root).
  settings(commonSettings: _*).
  settings(
    libraryDependencies ++= corenlpDependencies.map(_ % "compile"),
    libraryDependencies ++= sparkDependencies.map(_ % "compile")
  )