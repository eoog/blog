package com.www.pointservicebatch;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.www.pointservicebatch.domain.Point;
import com.www.pointservicebatch.domain.PointBalance;
import com.www.pointservicebatch.domain.PointType;
import com.www.pointservicebatch.repository.DailyPointReportRepository;
import com.www.pointservicebatch.repository.PointBalanceRepository;
import com.www.pointservicebatch.repository.PointRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "eureka.client.enabled=false",
    "spring.batch.job.enabled=false"
})
class PointBalanceSyncJobConfigTest {

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Mock
  private PointRepository pointRepository;

  @Mock
  private PointBalanceRepository pointBalanceRepository;

  @Autowired
  private DailyPointReportRepository dailyPointReportRepository;

  @Mock
  private RedissonClient redissonClient;

  @Mock
  private RMap<String, Long> balanceMap;

  @BeforeEach
  void setUp() {
    // Redis mock 설정
    when(redissonClient.<String, Long>getMap(anyString())).thenReturn(balanceMap);

    // 테스트 데이터 초기화
    dailyPointReportRepository.deleteAll();

    // 테스트 데이터 생성
    createTestData();
  }

  @Test
  @DisplayName("포인트 동기화 Job 실행 성공 테스트")
  void jobExecutionTest() throws Exception {
    // given
    JobParameters jobParameters = new JobParametersBuilder()
        .addString("datetime", LocalDateTime.now().toString())
        .toJobParameters();

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // then
    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
  }

  @Test
  @DisplayName("Redis 캐시 동기화 Step 테스트")
  void syncPointBalanceStepTest() throws Exception {
    // given
    PointBalance pointBalance = PointBalance.builder()
        .userId(1L)
        .balance(1000L)
        .createdAt(LocalDateTime.now())
        .build();
    when(pointBalanceRepository.findByUserId(1L)).thenReturn(java.util.Optional.of(pointBalance));

    JobParameters jobParameters = new JobParametersBuilder()
        .addString("datetime", LocalDateTime.now().toString())
        .toJobParameters();

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchStep("syncPointBalanceStep", jobParameters);

    // then
    assertThat(jobExecution.getStepExecutions()).hasSize(1);
    StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
    assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
  }

  @Test
  @DisplayName("일별 리포트 생성 Step 테스트")
  void generateDailyReportStepTest() throws Exception {
    // given
    LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
    List<Point> points = Arrays.asList(
        Point.builder()
            .userId(1L)
            .amount(1000L)
            .type(PointType.EARN)
            .balanceSnapshot(1000L)
            .createdAt(yesterday)
            .build()
    );
    when(pointRepository.findAllByCreatedAtBetween(
        yesterday.withHour(0).withMinute(0).withSecond(0),
        yesterday.withHour(23).withMinute(59).withSecond(59)
    )).thenReturn(points);

    JobParameters jobParameters = new JobParametersBuilder()
        .addString("datetime", LocalDateTime.now().toString())
        .toJobParameters();

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchStep("generateDailyReportStep", jobParameters);

    // then
    assertThat(jobExecution.getStepExecutions()).hasSize(1);
    StepExecution stepExecution = jobExecution.getStepExecutions().iterator().next();
    assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
  }

  private void createTestData() {
    // 테스트 데이터 설정
    LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
    List<Point> points = Arrays.asList(
        Point.builder()
            .userId(1L)
            .amount(1000L)
            .type(PointType.EARN)
            .balanceSnapshot(1000L)
            .createdAt(yesterday)
            .build()
    );
    when(pointRepository.findAllByCreatedAtBetween(
        yesterday.withHour(0).withMinute(0).withSecond(0),
        yesterday.withHour(23).withMinute(59).withSecond(59)
    )).thenReturn(points);
  }
}
