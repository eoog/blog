package com.www.msagraphql.model;

import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanSubscription {

  private Long id;
  private Long userId;
  private User user;
  private Long paymentId;
  private Payment payment;
  private String startDate;
  private String endDate;
  private String status; // Active, Expired

  public static PlanSubscription fromProto(EnrollmentServiceOuterClass.Subscription proto) {
    PlanSubscription subscription = new PlanSubscription();
    subscription.setId(proto.getSubscriptionId());
    subscription.setUserId(proto.getUserId());
    subscription.setPaymentId(proto.getPaymentId());
    subscription.setStatus(proto.getStatus());
    subscription.setStartDate(
        LocalDateTime.ofInstant(Instant.ofEpochSecond(proto.getStartDate()), ZoneId.systemDefault())
            .toString());
    subscription.setEndDate(
        LocalDateTime.ofInstant(Instant.ofEpochSecond(proto.getEndDate()), ZoneId.systemDefault())
            .toString());
    return subscription;
  }
}
