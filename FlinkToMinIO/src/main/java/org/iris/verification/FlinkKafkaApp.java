package org.iris.verification;


import DEMO.B2B_SMS.dbo.SMS_MT.Value;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import org.iris.verification.config.FlinkConfig;
import org.iris.verification.config.KafkaConfig;
import org.iris.verification.consumer.KafkaConsumerService;
import org.iris.verification.sink.ParquetSinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlinkKafkaApp {
    private static final Logger logger = LoggerFactory.getLogger(FlinkKafkaApp.class);

    public static void main(String[] args) {
        try {
            StreamExecutionEnvironment env = FlinkConfig.createExecutionEnvironment(2);
            KafkaSource<Value> kafkaSource = KafkaConfig.createKafkaSource();
            KafkaConsumerService.consumeKafkaStream(env, kafkaSource)
                    .addSink(ParquetSinkService.createParquetSink());
            env.execute("Flink Kafka CDC Consumer with MinIO Parquet Sink");
        } catch (Exception e) {
            logger.error("Lỗi khi chạy ứng dụng Flink Kafka", e);
            System.exit(1);
        }
    }
}
