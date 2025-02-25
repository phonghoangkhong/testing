package org.iris.verification.config;


import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FlinkConfig {
    public static StreamExecutionEnvironment createExecutionEnvironment(int parallelism) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(parallelism);
        env.enableCheckpointing(10000);
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(5000);
        env.getCheckpointConfig().setCheckpointTimeout(40000);
        return env;
    }
}
