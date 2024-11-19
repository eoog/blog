package com.www.back.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;

  @Autowired
  public BatchConfig(JobRepository jobRepository,
      PlatformTransactionManager platformTransactionManager) {
    this.jobRepository = jobRepository;
    this.platformTransactionManager = platformTransactionManager;
  }

  @Bean
  public Job exampleJob() {
    return new JobBuilder("exampleJob", jobRepository)
        .start(exampleStep())
        .build();
  }

  @Bean
  public Step exampleStep() {
    return new StepBuilder("exampleStep", jobRepository)
        .tasklet(exampleTasklet(), platformTransactionManager)
        .build();
  }

  @Bean
  public Tasklet exampleTasklet() {
    return (contribution, chunkContext) -> {
      System.out.println("Hello , Batch ");
      return RepeatStatus.FINISHED;
    };
  }
}
