name := "Scala-Spark"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq("junit" % "junit" % "3.8.1",
  "commons-io" % "commons-io" % "2.6",
  "net.minidev" % "json-smart" % "1.0.9",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.7",
  "org.apache.hadoop" % "hadoop-hdfs" % "2.7.0",
  "org.apache.hadoop" % "hadoop-auth" % "2.7.0",
  "org.apache.hadoop" % "hadoop-common" % "2.7.0",
  "org.apache.hadoop" % "hadoop-core" % "1.2.1",
  "io.netty" % "netty-all" % "4.1.17.Final",
  "org.apache.spark" % "spark-core_2.11" % "2.3.0",
  "org.apache.spark" % "spark-sql_2.11" % "2.3.0",
  "org.scala-lang" % "scala-library" % "2.11.1",
  "com.databricks" % "spark-xml_2.11" % "0.4.1")