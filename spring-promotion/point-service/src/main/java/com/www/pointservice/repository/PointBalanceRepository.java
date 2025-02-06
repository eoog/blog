package com.www.pointservice.repository;

import com.www.pointservice.domain.PointBalance;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface PointBalanceRepository extends JpaRepository<PointBalance, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE) // 쓰기락 !! 잠금
  Optional<PointBalance> findByUserId(Long userId);
}
