package com.www.msagraphql.service;

import com.example.msaenrollmentservice.domain.service.EnrollmentServiceGrpc;
import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass;
import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass.CourseRegistrationRequest;
import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass.UserEnrollmentsRequest;
import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass.UserSubscriptionsRequest;
import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass.UserSubscriptionsResponse;
import com.example.msaenrollmentservice.domain.service.FakePaymentServiceGrpc;
import com.www.msagraphql.model.Enrollment;
import com.www.msagraphql.model.Payment;
import com.www.msagraphql.model.PlanSubscription;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnrollmentService {

  @GrpcClient("msa-enrollment-service")
  private EnrollmentServiceGrpc.EnrollmentServiceBlockingStub enrollmentStub;
  @GrpcClient("msa-payment-service")
  private FakePaymentServiceGrpc.FakePaymentServiceBlockingStub paymentStub;

  /**
   * 강의 이용권 구매
   */

  public Payment purchaseCourse(long userId, long courseId, double amount, String paymentMethod) {
    Payment paymentResponse = createPayment(userId, "COURSE", amount, paymentMethod);
    registerCourse(userId, courseId, paymentResponse.getId());

    return paymentResponse;
  }

  /**
   * # 구독 결제 정보를 생성합니다.
   */

  public Payment purchaseSubscription(long userId, double amount, String paymentMethod) {
    Payment paymentResponse = createPayment(userId, "SUBSCRIPTION", amount, paymentMethod);
    manageSubscription(userId, System.currentTimeMillis(),
        System.currentTimeMillis() + 31536000000L, paymentResponse.getId());  // 1 year subscription

    return paymentResponse;
  }

  /**
   * 특정 사용자의 모든 등록 정보를 조회합니다.
   */
  public List<Enrollment> getEnrollmentsByUserId(Long userId) {
    UserEnrollmentsRequest request = UserEnrollmentsRequest.newBuilder()
        .setUserId(userId)
        .build();

    EnrollmentServiceOuterClass.UserEnrollmentsResponse response = enrollmentStub.getUserEnrollments(
        request);
    return response.getEnrollmentsList().stream()
        .map(proto -> Enrollment.fromProto(proto))
        .collect(Collectors.toList());
  }

  /**
   * 특정 사용자의 모든 구독 정보를 조회합니다.
   */
  public List<PlanSubscription> getSubscriptionsByUserId(Long userId) {
    EnrollmentServiceOuterClass.UserSubscriptionsRequest request = UserSubscriptionsRequest.newBuilder()
        .setUserId(userId)
        .build();

    UserSubscriptionsResponse resposne = enrollmentStub.getUserPlanSubscriptions(
        request);

    return resposne.getSubscriptionsList().stream()
        .map(plan -> PlanSubscription.fromProto(plan))
        .collect(Collectors.toList());

  }

  @Cacheable(value = "payment", key = "#paymentId")
  public Payment findPaymentById(Long paymentId) {
    EnrollmentServiceOuterClass.PaymentsByIdRequest request = EnrollmentServiceOuterClass.PaymentsByIdRequest.newBuilder()
        .setPaymentId(paymentId)
        .build();

    EnrollmentServiceOuterClass.PaymentsByIdResponse response = paymentStub.getPaymentsByPaymentId(
        request);
    return Payment.fromProto(response.getPayment());
  }

  /**
   * 구독 여부 확인
   */
  private EnrollmentServiceOuterClass.SubscriptionResponse manageSubscription(long userId,
      long startDate, long endDate, long paymentId) {
    EnrollmentServiceOuterClass.SubscriptionRequest request = EnrollmentServiceOuterClass.SubscriptionRequest.newBuilder()
        .setUserId(userId)
        .setStartDate(startDate)
        .setEndDate(endDate)
        .setPaymentId(paymentId)
        .build();
    return enrollmentStub.manageSubscription(request);
  }

  /**
   * # 특정 강의에 대한 권한을 가지고 있는지 여부를 체크합니다.
   */
  public boolean checkSubscriptionAccess(long userId) {
    EnrollmentServiceOuterClass.SubscriptionAccessRequest request = EnrollmentServiceOuterClass.SubscriptionAccessRequest.newBuilder()
        .setUserId(userId)
        .build();
    EnrollmentServiceOuterClass.SubscriptionAccessResponse response = enrollmentStub.checkSubscriptionAccess(
        request);
    return response.getHasAccess();
  }

  /**
   * # 특정 강의에 대한 권한을 가지고 있는지 여부를 체크합니다.
   */

  public boolean checkCourseAccess(long courseId, long userId) {
    EnrollmentServiceOuterClass.CourseAccessRequest request = EnrollmentServiceOuterClass.CourseAccessRequest.newBuilder()
        .setCourseId(courseId)
        .setUserId(userId)
        .build();
    EnrollmentServiceOuterClass.CourseAccessResponse response = enrollmentStub.checkCourseAccess(
        request);
    return response.getHasAccess();
  }


  /**
   * 이용권 결제 요청 Grpc
   */
  private Payment createPayment(long userId, String type, double amount, String paymentMethod) {
    EnrollmentServiceOuterClass.PaymentRequest request = EnrollmentServiceOuterClass.PaymentRequest.newBuilder()
        .setUserId(userId)
        .setType(type)
        .setAmount(amount)
        .setPaymentMethod(paymentMethod)
        .build();
    return Payment.fromProto(paymentStub.createPayment(request));
  }

  /**
   * 강의 등록 요청 Grpc
   */
  private EnrollmentServiceOuterClass.CourseRegistrationResponse registerCourse(long userId,
      long courseId, long paymentId) {
    CourseRegistrationRequest reqeust = CourseRegistrationRequest.newBuilder()
        .setUserId(userId)
        .setCourseId(courseId)
        .setPaymentId(paymentId)
        .build();

    return enrollmentStub.registerCourse(reqeust);
  }
}
