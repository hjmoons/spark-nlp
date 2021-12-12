lazy val commonSettings = Seq(
  name := "spark-nlp",
  version := "0.1",
  scalaVersion := "2.12.7"
)

lazy val app = (project in file(".")).
  settings(commonSettings: _*)

libraryDependencies ++= Seq(
  // https://mvnrepository.com/artifact/org.apache.spark/spark-sql
  "org.apache.spark" %% "spark-sql" % "3.2.0" % "provided",
)

mainClass in assembly := Some("nlp.NLPMain")
assemblyJarName := "nlp.jar"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}