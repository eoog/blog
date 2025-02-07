package com.www.pointservicebatch.repository;

import com.www.pointservicebatch.domain.PointBalance;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointBalanceRepository extends JpaRepository<PointBalance, Long> {
  Optional<PointBalance> findByUserId(Long userId);
}
