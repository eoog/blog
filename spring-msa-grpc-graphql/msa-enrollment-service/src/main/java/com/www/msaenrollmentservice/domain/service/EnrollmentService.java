package com.www.msaenrollmentservice.domain.service;

import com.www.msaenrollmentservice.domain.entity.Enrollment;
import com.www.msaenrollmentservice.domain.entity.Subscription;
import com.www.msaenrollmentservice.domain.repository.EnrollmentRepository;
import com.www.msaenrollmentservice.domain.repository.SubscriptionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {
  private final EnrollmentRepository enrollmentRepository;

  private final SubscriptionRepository subscriptionRepository;

  public EnrollmentService(EnrollmentRepository enrollmentRepository, SubscriptionRepository subscriptionRepository) {
    this.enrollmentRepository = enrollmentRepository;
    this.subscriptionRepository = subscriptionRepository;
  }

  public Enrollment registerCourse(long userId, long courseId, long paymentId) {
    Enrollment enrollment = new Enrollment();
    enrollment.setUserId(userId);
    enrollment.setCourseId(courseId);
    enrollment.setPaymentId(paymentId);
    enrollment.setRegistrationDate(LocalDateTime.now());
    return enrollmentRepository.save(enrollment);
  }

  public Subscription manageSubscription(long userId, LocalDateTime startDate, LocalDateTime endDate, long paymentId) {
    Subscription subscription = new Subscription();
    subscription.setUserId(userId);
    subscription.setStartDate(startDate);
    subscription.setEndDate(endDate);
    subscription.setPaymentId(paymentId);
    return subscriptionRepository.save(subscription);
  }

  public Subscription renewSubscription(long subscriptionId, LocalDateTime newStartDate, LocalDateTime newEndDate) {
    Subscription subscription = subscriptionRepository.findById(subscriptionId)
        .orElseThrow(() -> new IllegalStateException("Subscription not found with id: " + subscriptionId));
    subscription.setStartDate(newStartDate);
    subscription.setEndDate(newEndDate);
    return subscriptionRepository.save(subscription);
  }

  public boolean checkCourseAccess(long userId, long courseId) {
    Optional<Enrollment> enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);
    return enrollment.isPresent();
  }

  public boolean checkSubscriptionAccess(long userId, LocalDateTime now) {
    Optional<Subscription> subscription = subscriptionRepository.findTopByUserIdAndEndDateAfterOrderByEndDateDesc(userId, now);
    return subscription.isPresent() && !subscription.get().getEndDate().isBefore(now);
  }

  public List<Enrollment> getUserEnrollments(long userId) {
    return enrollmentRepository.findAllByUserId(userId);
  }

  public List<Subscription> getUserPlanSubscriptions(long userId) {
    return subscriptionRepository.findAllByUserId(userId);
  }
}
