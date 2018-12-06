# XML Processing using Spark - Hadoop - YARN

This is a Proof Of Concept (POC) project to process XML files in Hadoop HDFS directory using Apache Spark.

POC has been implemented in Scala and Java.

Below steps will guide end-to-end from setup of Hadoop, YARN & Spark and execution of the Scala & Java code using spark-submit.

**Setup Hadoop & YARN**

**1.** wget http://apache.claz.org/hadoop/common/stable2/hadoop-2.9.2.tar.gz 

tar -xvf hadoop-2.9.2.tar.gz

mv hadoop-2.9.2 hadoop

export **HADOOP\_CONF_DIR**=*<hadoop\_base_dir>*/hadoop/etc/hadoop

a. Edit **$HADOOP\_CONF_DIR**/core-site.xml as:

    <configuration>
       <property>
       <name>fs.default.name</name>
       <value>hdfs://node-master:9000</value>   
       </property>
    </configuration>

 where host-name: "node-master" entry should be made in /etc/hosts like:
10.180.3.5  node-master

 b. Edit **$HADOOP\_CONF_DIR**/hdfs-site.xml as:

    <configuration>
       <property>
        <name>dfs.namenode.name.dir</name>
        <value>/scratch/muamir/hadoop/data/nameNode</value>
       </property>
    </configuration>

 c. Setting YARN as Job Scheduler and default framework for MapReduce operations

  At **$HADOOP\_CONF_DIR**, rename mapred-site.xml.template to mapred-site.xml:

  mv mapred-site.xml.template mapred-site.xml

  Edit **$HADOOP\_CONF_DIR**/mapred-site.xml as:

    <configuration>
       <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
       </property>
    </configuration>

  To configure YARN, edit **$HADOOP\_CONF_DIR**/yarn-site.xml

    <configuration>
       <property>
         <name>yarn.acl.enable</name>
         <value>0</value>
       </property>
       <property>
         <name>yarn.resourcemanager.hostname</name>
         <value>node-master</value>
       </property>
       <property>
         <name>yarn.nodemanager.aux-services</name>
         <value>mapreduce_shuffle</value>
       </property>
       <property>
         <name>yarn.nodemanager.pmem-check-enabled</name>
         <value>false</value>
       </property>
       <property>
         <name>yarn.nodemanager.vmem-check-enabled</name>
        <value>false</value>
       </property>
    </configuration>


  **Environment Variables:**

  export PATH=$PATH:*<hadoop\_base_dir>*/bin:*<hadoop\_base\_dir>*/sbin
export LD\_LIBRARY\_PATH=*<hadoop\_base_dir>*/lib/native:$LD\_LIBRARY\_PATH
    
  HDFS needs to be formatted like any classical file system. On node-master, run the following commands:

  hdfs namenode -format

  hdfs getconf -namenodes

  Start the HDFS by running the following script from node-master:

  start-dfs.sh

  Check that every process is running with the jps (Jvm Process Status) command on each node. On node-master, it should list like:

  22820 Jps

  18650 DataNode

  18872 SecondaryNameNode

  18380 NameNode

  Monitor HDFS Cluster

  hdfs dfsadmin -report

  hdfs dfsadmin -help


  http://*<host\_name>*:50070

  **Create content at HDFS:**

  hdfs dfs -mkdir inputs

  hdfs dfs -put \`ls <dir>/*.xml\` inputs

  hdfs dfs -ls inputs

**NOTE:**
In case if content at hdfs gets content, some useful commands:

  hdfs dfsadmin -safemode leave
  hdfs fsck -list-corruptfileblocks
  hdfs fsck -delete


**Setup Spark**

wget https://d3kbcqa49mib13.cloudfront.net/spark-2.2.0-bin-hadoop2.7.tgz

tar -xvf spark-2.2.0-bin-hadoop2.7.tgz

mv spark-2.2.0-bin-hadoop2.7 spark

**Environment Variables:**

export **SPARK_HOME**=*<spark\_base\_dir>*

export PATH=$PATH:**$SPARK_HOME**/bin

Rename the spark default template config file:

mv **$SPARK\_HOME**/conf/spark-defaults.conf.template **$SPARK_HOME**/conf/spark-defaults.conf

Edit $SPARK_HOME/conf/spark-defaults.conf and set spark.master to yarn:

spark.master    yarn

**To Start Yarn:** start-yarn.sh

**Spark Shell Command:**
spark-shell --master yarn --deploy-mode client

To run Spark Example Code:

spark-submit --deploy-mode client \--class org.apache.spark.examples.SparkPi $SPARK\_HOME/examples/jars/spark-examples_2.11-2.2.0.jar 10
 
**Spark UI:**

http://*<ip_address>*:4040/

http://*<ip_address>*:8088/proxy/<application\_id>/jobs/
