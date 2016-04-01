### A Simple Spark Streaming App 

This is a simple spark streaming application which get the streams from Twitter and would run on Mesos cluster.

## Requirements:
- Run spark-dispatcher on Mesos
- Run Cassandra clusters on Mesos
- In Cassandra, a keystores, named test, was created. In test keystore, a table, named twitter, was created.
`Create table twitter(tid bigint, tweet text, createdAt bigint,PRIMARY KEY(tid));`
- Put related jar files in each Mesos slaves 
1) spark-cassandra-connector-assembly-1.5.0.jar
2) spark-streaming-twitter_2.10-1.6.0.jar
3) twitter4j-core-4.0.4.jar
4) twitter4j-stream-4.0.4.jar
- For the machine who submit the spark job, in spark-defaults.conf, add the entry about spark.executor.extraClassPath
i.e. the slave would find the related jar files
e.g. 
spark.executor.extraClassPath /path/to/spark-cassandra-connector-assembly-1.5.0.jar:/path/to/spark-streaming-twitter_2.10-1.6.0.jar:/other/jar/path:....

## Source code:

In the source code, you are required to put the following values:
- YOUR_CONSUMERKEY
- YOUR_CONSUMERSECRET
- YOUR_ACCESSTOKEN
- YOUR_ACCESSTOKENSECRET
- IP_ADDRESS_OF_CASSANDRA

Those information can be obtained after register an account from https://apps.twitter.com/

## Build the jar

To build the jar file, issue the following command: `sbt assembly`

## Submit the spark job
1) Share the jar file, i.e. the slaves would access it 
[A simple way is share it by http, e.g. `python -m SimpleHTTPServer`]

2) Go to spark bin directory and issue the following command:
`./spark-submit --class DemoApp  --master mesos://<mesos spark dispatcher ip>:7077 --deploy-mode cluster --executor-memory 512m --total-executor-cores 3 http://<ip address to access the jar file>/DemoProject-assembly-1.0.jar`

*Note: the executor-cores must be greater than 1. Otherwise, the application does not work in cluster mode.*