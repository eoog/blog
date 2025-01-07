package com.www.msaenrollmentservice.domain.repository;

import com.www.msaenrollmentservice.domain.entity.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findByUserId(long userId);
}
