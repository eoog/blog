package com.www.msagraphql.model;

import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment implements Serializable {

  private Long id;
  private Long userId;
  private User user;
  private String paymentType;
  private Float amount;
  private String paymentMethod;
  private String paymentDate;

  /**
   * Stub Request 요청 보냈지만 응답값으로 Resposne 값 PaymentRequest >> PaymentResponse 변경된거 확인
   */
  public static Payment fromProto(EnrollmentServiceOuterClass.PaymentResponse proto) {
    Payment payment = new Payment();
    payment.setId(proto.getPaymentId());
    payment.setUserId(proto.getUserId());
    payment.setPaymentType(proto.getType());
    payment.setAmount((float) proto.getAmount());
    payment.setPaymentMethod(proto.getPaymentMethod());
    payment.setPaymentDate(
        Instant.ofEpochMilli(proto.getPaymentDate()).atZone(ZoneId.systemDefault()).format(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    return payment;
  }
}
