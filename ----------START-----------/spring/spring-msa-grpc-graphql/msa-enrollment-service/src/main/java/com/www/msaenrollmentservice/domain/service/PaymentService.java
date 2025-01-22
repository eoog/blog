package com.www.msaenrollmentservice.domain.service;

import com.www.msaenrollmentservice.domain.entity.Payment;
import com.www.msaenrollmentservice.domain.entity.PaymentType;
import com.www.msaenrollmentservice.domain.repository.PaymentRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

  private final PaymentRepository paymentRepository;

  public PaymentService(PaymentRepository paymentRepository) {
    this.paymentRepository = paymentRepository;
  }

  public Payment createPayment(Long userId, PaymentType type, BigDecimal amount, String paymentMethod) {
    Payment payment = new Payment();
    payment.setUserId(userId);
    payment.setPaymentType(type);
    payment.setAmount(amount);
    payment.setPaymentMethod(paymentMethod);
    payment.setPaymentDate(LocalDateTime.now());
    return paymentRepository.save(payment);
  }

  public Payment getPaymentById(Long paymentId) {
    return paymentRepository.findById(paymentId).orElse(null);
  }

  @Transactional
  public Payment updatePaymentMethod(Long paymentId, String newPaymentMethod) {
    Payment payment = paymentRepository.findById(paymentId).orElse(null);
    if (payment != null) {
      payment.setPaymentMethod(newPaymentMethod);
      paymentRepository.save(payment);
    }
    return payment;
  }

  public List<Payment> getUserPayments(long userId) {
    return paymentRepository.findByUserId(userId);
  }
}
