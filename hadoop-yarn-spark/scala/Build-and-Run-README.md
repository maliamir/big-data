**Build Code**

*Pre-requisite:* sbt (Simple Build Tool for Scala)

At directory where build.sbt (Scala-Spark/) resides, do

**sbt compile**

**sbt publishLocal**

which will generate jars at path like <user_dir>/../scala-spark_2.11/0.1/jars as:

**scala-spark_2.11.jar**

****

**Running XML Processing Code on Spark**

spark-submit --deploy-mode client --class PerformanceEvaluationApp --jars target/java-spark-1.0-SNAPSHOT-jar-with-dependencies.jar,scala-spark_2.11.jar 10

where target/java-spark-1.0-SNAPSHOT-jar-with-dependencies.jar is the same jar as generated from the spark-java code which has all required libraries for hadoop, spark, apache-commons, json etc.