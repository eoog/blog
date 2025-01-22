package com.www.msaenrollmentservice.domain.entity;

import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Data;

  @Data
  @Entity
  @Table(name = "subscriptions")
  public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", insertable = false, updatable = false)
    private Payment payment;

    public EnrollmentServiceOuterClass.Subscription toProto() {
      LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
      String status = (now.isAfter(this.startDate) && now.isBefore(this.endDate)) ? "Active" : "Expired";

      return EnrollmentServiceOuterClass.Subscription.newBuilder()
          .setSubscriptionId(this.subscriptionId)
          .setUserId(this.userId)
          .setPaymentId(this.paymentId)
          .setStartDate(this.startDate.atZone(ZoneId.systemDefault()).toEpochSecond())
          .setEndDate(this.endDate.atZone(ZoneId.systemDefault()).toEpochSecond())
          .setStatus(status)
          .build();
    }
}
