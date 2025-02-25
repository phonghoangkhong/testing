package org.iris.verification.config;



import DEMO.B2B_SMS.dbo.SMS_MT.Value;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.iris.verification.util.PropertyLoader;

public class KafkaConfig {
    private static final String KAFKA_BROKER = PropertyLoader.get("KAFKA_BROKER");
    private static final String TOPIC = PropertyLoader.get("TOPIC");
    private static final String GROUP_ID = PropertyLoader.get("GROUP_ID");
    private static final String SCHEMA_REGISTRY_URL = PropertyLoader.get("SCHEMA_REGISTRY_URL");

    public static KafkaSource<Value> createKafkaSource() {
        ConfluentRegistryAvroDeserializationSchema<Value> deserializationSchema =
                ConfluentRegistryAvroDeserializationSchema.forSpecific(
                        Value.class,  // Class được generate từ Avro schema
                        SCHEMA_REGISTRY_URL
                );

        return KafkaSource.<Value>builder()
                .setBootstrapServers(KAFKA_BROKER)
                .setTopics(TOPIC)
                .setGroupId(GROUP_ID)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(deserializationSchema)
                .setProperty("enable.auto.commit", "false") // Disable auto commit
                .setProperty("auto.offset.reset", "earliest")
                .build();
    }
}
