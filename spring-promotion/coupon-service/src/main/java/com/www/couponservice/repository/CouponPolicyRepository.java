package com.www.couponservice.repository;

import com.www.couponservice.domain.CouponPolicy;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT cp FROM CouponPolicy cp WHERE cp.id = :id")
  Optional<CouponPolicy> findByIdWithLock(Long id);
}
