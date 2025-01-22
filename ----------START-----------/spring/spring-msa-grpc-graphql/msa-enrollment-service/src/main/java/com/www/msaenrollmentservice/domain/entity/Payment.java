package com.www.msaenrollmentservice.domain.entity;

import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Data;

@Data
@Entity
@Table(name = "payments")
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Long paymentId;

  @Column(nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentType paymentType;

  @Column(nullable = false)
  private BigDecimal amount;

  @Column(nullable = false)
  private String paymentMethod;

  @Column(nullable = false)
  private LocalDateTime paymentDate;

  public EnrollmentServiceOuterClass.PaymentResponse toProto() {
    EnrollmentServiceOuterClass.PaymentResponse.Builder builder = EnrollmentServiceOuterClass.PaymentResponse.newBuilder();

    if (this.paymentId != null) {
      builder.setPaymentId(this.paymentId);
      builder.setPaymentSuccessful(this.paymentId != null);
    }
    if (this.userId != null) {
      builder.setUserId(this.userId);
    }
    if (this.paymentType != null) {
      builder.setType(this.paymentType.name());
    }
    if (this.amount != null) {
      builder.setAmount(this.amount.doubleValue());
    }
    if (this.paymentMethod != null) {
      builder.setPaymentMethod(this.paymentMethod);
    }
    if (this.paymentDate != null) {
      builder.setPaymentDate(this.paymentDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    return builder.build();
  }

}
