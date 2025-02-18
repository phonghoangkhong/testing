package org.example;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;

import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class SparkKafkaConsumer {
    public static void main(String[] args) throws TimeoutException, StreamingQueryException {
        System.setProperty("hadoop.home.dir", "D:\\winutils\\hadoop-3.0.0");

        // Khởi tạo SparkSession
        SparkSession spark = SparkSession.builder().appName("Testing Spark Consumer")
                .master("local[*]")
                .config("deploy-mode", "client")
               .getOrCreate();
        SparkContext sc = spark.sparkContext();
        sc.setLogLevel("ERROR");
//        Dataset<Row> df = spark.read().csv("src/main/resources/username.csv");
//        df.show();
        // Đọc dữ liệu từ Kafka
        Dataset<Row> kafkaStream = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "172.31.17.135:9092") // Kafka Server
                .option("subscribe", "quickstart-events") // Tên topic Kafka
                .option("startingOffsets", "earliest") // Đọc từ đầu topic
                .option("group.id", "testing-v23")
                .load() ;

//        // Chuyển đổi dữ liệu từ byte sang chuỗi
        Dataset<Row> messages = kafkaStream.selectExpr("CAST(value AS STRING)");
        StreamingQuery query = messages
                .writeStream()
                .outputMode("update") // Chế độ update
                .format("console") // Xuất ra console
                .start();


//        Dataset<Row> lines = spark
//                .readStream()
//                .format("socket")
//                .option("host", "172.31.17.135")
//                .option("port", 9999)
//                .load();
//        Dataset<String> words = lines
//                .as(Encoders.STRING())
//                .flatMap((FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(), Encoders.STRING());

// Generate running word count
//        Dataset<Row> wordCounts = words.groupBy("value").count();
//        StreamingQuery query = wordCounts.writeStream()
//                .outputMode("complete")
//                .format("console")
//                .start();

        query.awaitTermination();
    }
}
