package org.iris.verification.consumer;


import DEMO.B2B_SMS.dbo.SMS_MT.Value;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class KafkaConsumerService {
    public static DataStream<Value> consumeKafkaStream(StreamExecutionEnvironment env, KafkaSource<Value> source) {
        return env.fromSource(source,
                org.apache.flink.api.common.eventtime.WatermarkStrategy.noWatermarks(),
                "Kafka CDC Source");
    }
}
