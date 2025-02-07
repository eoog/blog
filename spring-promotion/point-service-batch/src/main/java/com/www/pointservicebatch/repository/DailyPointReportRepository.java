package com.www.pointservicebatch.repository;

import com.www.pointservicebatch.domain.DailyPointReport;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyPointReportRepository extends JpaRepository<DailyPointReport, Long> {
  List<DailyPointReport> findByReportDate(LocalDate reportDate);
  List<DailyPointReport> findByUserIdAndReportDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
