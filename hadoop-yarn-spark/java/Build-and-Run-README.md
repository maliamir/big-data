**Build Code**

*Pre-requisite:* maven and related environment variables are set

At directory where pom.xml resides, do

**mvn clean install**


**Running XML Processing Code on Spark**

spark-submit --deploy-mode client --class com.maaksoft.spark.PerformanceEvaluationApp target/java-spark-1.0-SNAPSHOT-jar-with-dependencies.jar 10