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
                .option("kafka.bootstrap.servers", "kafka0:9092") // Kafka Server
                .option("subscribe", "DEMO3.B2B_SMS.dbo.SMS_MT") // Tên topic Kafka
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



        query.awaitTermination();
    }
}
