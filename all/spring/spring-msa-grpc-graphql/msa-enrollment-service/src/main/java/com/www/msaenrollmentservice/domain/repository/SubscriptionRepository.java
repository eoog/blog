package com.www.msaenrollmentservice.domain.repository;

import com.www.msaenrollmentservice.domain.entity.Subscription;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  Optional<Subscription> findTopByUserIdAndEndDateAfterOrderByEndDateDesc(long userId, LocalDateTime now);

  List<Subscription> findAllByUserId(long userId);
}
