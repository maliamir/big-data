import java.util.Date
import java.util

import java.io.IOException
import java.io.File
import java.io.FileOutputStream

import java.net.URI

import org.apache.commons.io.IOUtils

import net.minidev.json.JSONArray
import net.minidev.json.JSONObject

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

import org.apache.hadoop.conf.Configuration

import org.apache.hadoop.fs.{FileSystem, LocalFileSystem, Path}

import org.apache.hadoop.hdfs.DistributedFileSystem

import org.apache.spark.SparkConf

import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row


object PerformanceEvaluationApp {

  @throws[IOException]
  def main(args: Array[String]): Unit = {

    val stringBuilder = new StringBuilder

    try {

      val configuration = new Configuration
      configuration.set("fs.hdfs.impl", classOf[DistributedFileSystem].getName)
      configuration.set("fs.file.impl", classOf[LocalFileSystem].getName)

      val fileSystem = FileSystem.get(URI.create("hdfs://node-master:9000"), configuration)
      val inputsDirectoryPath = new Path("inputs")
      val fileStatuses = fileSystem.listStatus(inputsDirectoryPath)

      val sparkConf = new SparkConf().setAppName("Performance Evaluation Application").setMaster("local[*]")
      val javaSparkContext = new JavaSparkContext(sparkConf)

      val testsDurationMap = new util.LinkedHashMap[String, TestRecord]
      val startTime = new Date().getTime

      for (fileStatus <- fileStatuses) {

        val xmlFile = fileStatus.getPath.toString
        stringBuilder.append("XML File: " + xmlFile + "\n")

        val sqlContext = new SQLContext(javaSparkContext)
        val rowsDataset = sqlContext.read.format("com.databricks.spark.xml").option("rootTag", "test-log").option("rowTag", "test-result").load(xmlFile)

        val rows = rowsDataset.collect.asInstanceOf[Array[Row]]

        for (row <- rows) {

          val logicalName = row.get(row.fieldIndex("_logicalname")).asInstanceOf[String]

          var testRecord = testsDurationMap.get(logicalName)
          if (testRecord == null) {
            testRecord = new TestRecord(logicalName)
          }

          val duration = row.get(row.fieldIndex("_duration")).asInstanceOf[Long]
          testRecord.addDuration(duration)

          testsDurationMap.put(logicalName, testRecord)

        }

      }

      val endTime = new Date().getTime
      val timeTaken = ((endTime - startTime).toDouble) / 1000
      stringBuilder.append("Time taken: " + timeTaken + " seconds.\n")

      val jsonArray = new JSONArray
      val logicalNamesItr = testsDurationMap.keySet.iterator
      while ( {logicalNamesItr.hasNext}) {

        val logicalName = logicalNamesItr.next
        val testRecord = testsDurationMap.get(logicalName)
        val avgDuration = testRecord.getDuration.toDouble / testRecord.getOccurrences

        val jsonObject = new JSONObject
        jsonObject.put("logicalName", logicalName)
        jsonObject.put("averageDuration", avgDuration.toString)
        jsonArray.add(jsonObject)

      }

      val objectMapper = new ObjectMapper
      objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
      val json = objectMapper.readValue(jsonArray.toString, classOf[Any])
      stringBuilder.append(objectMapper.writeValueAsString(json))

    } finally try {

      val fileOutputStream = new FileOutputStream(new File("/scratch/muamir/hadoop/spark/code/source_code/SparkXml/java-project/scala-run-output.log"))

      try
        IOUtils.write(stringBuilder.toString, fileOutputStream, "UTF-8")
      finally if (fileOutputStream != null) fileOutputStream.close()

    }

  }

}