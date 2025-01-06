package com.www;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class Main {

  public static void main(String[] args) throws Exception {
    // 1. Flink 실행 환경 설정
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    // 2. Kafka 소스 설정
    KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
        .setBootstrapServers("localhost:9092")
        .setTopics("source_topic")
        .setGroupId("flink_service")
        .setStartingOffsets(OffsetsInitializer.earliest())  // 처음부터 데이터를 읽음
        .setValueOnlyDeserializer(new SimpleStringSchema())
        .build();

    // 3. JSON 문자열을 WebLog 객체로 변환 (Jackson 사용)
    DataStream<WebLog> webLogStream = env.fromSource(
        kafkaSource,
        WatermarkStrategy.forMonotonousTimestamps(),  // Ingestion Time 설정
        "Kafka Source"
    ).map(jsonString -> {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(jsonString, WebLog.class);  // JSON 문자열을 WebLog 객체로 변환
    });

    // 4. 누적 카운트 (상태 기반)
    DataStream<String> cumulativeAnomalies = webLogStream
        .map(log -> new Tuple2<>(log.getIpAddress(), 1))  // IP별로 카운팅
        .returns(Types.TUPLE(Types.STRING, Types.INT))
        .keyBy(value -> value.f0)  // IP별로 그룹핑
        .process(new AnomalyDetectionProcessFunction(300));  // 누적 카운트 처리

    // 5. 윈도우 기반 카운트
    DataStream<String> windowedAnomalies = webLogStream
        .map(log -> new Tuple2<>(log.getIpAddress(), 1))  // IP별로 카운팅
        .returns(Types.TUPLE(Types.STRING, Types.INT))
        .keyBy(value -> value.f0)  // IP별로 그룹핑
        .window(SlidingEventTimeWindows.of(Time.minutes(1), Time.seconds(10)))  // 1분 윈도우, 10초 슬라이딩
        .sum(1)
        .filter(ipCount -> ipCount.f1 > 50)  // 임계치 초과 여부 필터링
        .map(ipCount -> "Windowed Anomaly detected from IP: " + ipCount.f0 + " with count: " + ipCount.f1);  // 윈도우 기반 메시지 생성

    // 6. Kafka Sink 설정
    KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
        .setBootstrapServers("localhost:9092")
        .setRecordSerializer(KafkaRecordSerializationSchema.builder()
            .setTopic("anomalies")
            .setValueSerializationSchema(new SimpleStringSchema())
            .build())
        .build();

    // 누적 카운트와 윈도우 카운트 모두 동일한 Kafka 토픽에 내보냄
    cumulativeAnomalies.sinkTo(kafkaSink);
    windowedAnomalies.sinkTo(kafkaSink);

    // 7. Flink 작업 실행
    env.execute("Web Log Anomaly Detection with Kafka");
  }
}