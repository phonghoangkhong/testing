package org.iris.verification.sink;

import DEMO.B2B_SMS.dbo.SMS_MT.Value;
import org.apache.flink.core.fs.Path;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.BasePathBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.iris.verification.util.PropertyLoader;


import java.text.SimpleDateFormat;

public class ParquetSinkService {
    private static final String MINIO_BUCKET = PropertyLoader.get("MINIO_BUCKET");
    private static final String BASE_PATH = PropertyLoader.get("BASE_PATH");
    private static final String FILE_PREFIX = PropertyLoader.get("FILE_PREFIX");

    static class DateBucketAssigner implements BucketAssigner<Value, String> {
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public String getBucketId(Value element, Context context) {
            String requestTime = element.getRequestTime();
            return BASE_PATH + "/" + requestTime.split("\\.")[0];
        }

        @Override
        public SimpleVersionedSerializer<String> getSerializer() {
            return new BasePathBucketAssigner().getSerializer();
        }
    }

    public static StreamingFileSink<Value> createParquetSink() {
        return StreamingFileSink
                .forBulkFormat(new Path(MINIO_BUCKET + "/" + BASE_PATH),
                        org.apache.flink.formats.parquet.avro.ParquetAvroWriters.forSpecificRecord(Value.class))
                .withBucketAssigner(new DateBucketAssigner())
                .withRollingPolicy(OnCheckpointRollingPolicy.build())
                .withOutputFileConfig(OutputFileConfig.builder()
                        .withPartPrefix(FILE_PREFIX)
                        .withPartSuffix(".snappy.parquet")
                        .build())
                .build();
    }
}

