package com.maaksoft.spark;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;

import java.net.URI;

import org.apache.commons.io.IOUtils;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class PerformanceEvaluationApp {

    public static void main(String[] args) throws IOException {

        final StringBuilder stringBuilder = new StringBuilder();
        
        try {            

            Configuration configuration = new Configuration();
            configuration.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            configuration.set("fs.file.impl",org.apache.hadoop.fs.LocalFileSystem.class.getName());
            FileSystem fileSystem = FileSystem.get(URI.create("hdfs://node-master:9000"), configuration);
            Path inputsDirectoryPath = new Path("inputs");
            FileStatus[] fileStatuses = fileSystem.listStatus(inputsDirectoryPath);
            
            SparkSession sparkSession = SparkSession.builder().appName(PerformanceEvaluationApp.class.getName()).master("local").getOrCreate();
            LinkedHashMap<String, TestRecord> testsDurationMap = new LinkedHashMap<String, TestRecord>();

            long startTime = new Date().getTime();            
            for (FileStatus fileStatus: fileStatuses) {
                
                String xmlFile = fileStatus.getPath().toString();
                stringBuilder.append("XML File: " + xmlFile + "\n");
                SQLContext sqlContext = new SQLContext(sparkSession);
                Dataset<Row> rowsDataset = sqlContext.read().format("com.databricks.spark.xml").option("rootTag", 
                                                                                                       "test-log").option("rowTag", 
                                                                                                                          "test-result").load(xmlFile);                            
                /*
                String[] fieldNames = rowsDataset.schema().fieldNames();            
                for (int index = 0; index < fieldNames.length; index++) {
                    stringBuilder.append(fieldNames[index] + ",");
                }
                stringBuilder.append("\n\n");
                */
                
                Row[] rows = (Row[]) rowsDataset.collect();
                for (Row row : rows) {
                    
                    String logicalName = (String)row.get(row.fieldIndex("_logicalname")); 
                    Object durationObj = row.get(row.fieldIndex("_duration"));
                    Long duration = ((durationObj == null) ? 0 : Long.valueOf(durationObj.toString())); 
                    
                    TestRecord testRecord = testsDurationMap.get(logicalName);
                    if (testRecord == null) {
                        testRecord = new TestRecord(logicalName);
                    }
                    testRecord.addDuration(duration);
                    testsDurationMap.put(logicalName, testRecord);
                    
                }
                        
            }
            
            long endTime = new Date().getTime();
            double timeTaken = (((double)(endTime - startTime))  / 1000);
            stringBuilder.append("Time taken: " + timeTaken + " seconds.\n");
            
            JSONArray jsonArray = new JSONArray();
            Iterator<String> logicalNamesItr = testsDurationMap.keySet().iterator();
            while (logicalNamesItr.hasNext()) {
                                    
                String logicalName = logicalNamesItr.next();
                TestRecord testRecord = testsDurationMap.get(logicalName);
                double avgDuration = ((double)testRecord.getDuration()) / testRecord.getOccurrences();
                
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("logicalName", logicalName);
                jsonObject.put("averageDuration", avgDuration);
                jsonArray.add(jsonObject);
                
            }
            
            ObjectMapper objectMapper  = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            Object json = objectMapper.readValue(jsonArray.toString(), Object.class);
            stringBuilder.append(objectMapper.writeValueAsString(json));     
            
        } finally {
            
            try (FileOutputStream fileOutputStream = new FileOutputStream(new File("/home/user/java-spark-run-output.log"));){
                IOUtils.write(stringBuilder.toString(), fileOutputStream);
            }
        
        }
        
    }

}