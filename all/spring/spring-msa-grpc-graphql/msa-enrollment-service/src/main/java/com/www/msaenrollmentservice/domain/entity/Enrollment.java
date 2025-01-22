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
  @Table(name = "enrollments")
  public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long courseId;

    @Column(name = "payment_id" ,nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", insertable = false, updatable = false)
    private Payment payment;

    public EnrollmentServiceOuterClass.Enrollment toProto() {
      return EnrollmentServiceOuterClass.Enrollment.newBuilder()
          .setEnrollmentId(this.enrollmentId)
          .setUserId(this.userId)
          .setCourseId(this.courseId)
          .setPaymentId(this.paymentId)
          .setRegistrationDate(this.registrationDate.atZone(ZoneId.systemDefault()).toEpochSecond())
          .build();
    }

}
