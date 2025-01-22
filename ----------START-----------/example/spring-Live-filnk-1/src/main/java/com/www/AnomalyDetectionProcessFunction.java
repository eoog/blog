package com.www;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

public class AnomalyDetectionProcessFunction extends
    KeyedProcessFunction<String, Tuple2<String, Integer>, String> {

  private final int threshold;  // 임계치
  private transient ValueState<Integer> countState;  // 상태 관리

  public AnomalyDetectionProcessFunction(int threshold) {
    this.threshold = threshold;
  }

  @Override
  public void open(Configuration parameters) throws Exception {
    // 상태 초기화 (IP별 접속 횟수를 저장)
    ValueStateDescriptor<Integer> countDescriptor = new ValueStateDescriptor<>("ipCount", Integer.class);
    countState = getRuntimeContext().getState(countDescriptor); // 이렇게 하면 count가 계속적으로 누적됨
  }

  @Override
  public void processElement(Tuple2<String, Integer> value, Context ctx, Collector<String> out) throws Exception {
    // 현재 상태에서 접속 횟수 가져오기
    Integer currentCount = countState.value();
    if (currentCount == null) {
      currentCount = 0;
    }

    // 새로운 접속 횟수 더하기
    currentCount += value.f1;

    // 상태 업데이트
    countState.update(currentCount);

    System.out.println("currentCount = " + currentCount);

    // 임계치를 초과하는 경우 이상 탐지로 간주하여 결과 출력
    if (currentCount > threshold) {
      out.collect("Anomaly detected from IP: " + value.f0 + " with count: " + currentCount);
    }
  }
}