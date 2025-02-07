package com.www.pointservicebatch.repository;

import com.www.pointservicebatch.domain.Point;
import io.lettuce.core.dynamic.annotation.Param;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PointRepository extends JpaRepository<Point, Long> {
  @Query("SELECT p FROM Point p WHERE p.createdAt BETWEEN :startDate AND :endDate")
  List<Point> findAllByCreatedAtBetween(
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate);
}
