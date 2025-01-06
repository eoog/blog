package com.www.springlogbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class BatchScheduler {

  private JobLauncher jobLauncher;

  private Job WebLog;

  public BatchScheduler(JobLauncher jobLauncher, Job webLog) {
    this.jobLauncher = jobLauncher;
    WebLog = webLog;
  }

  @Scheduled(fixedDelay = 10000) // 10초마다
  public void runBatchJon()
      throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
    jobLauncher.run(WebLog,new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
  }
  }

