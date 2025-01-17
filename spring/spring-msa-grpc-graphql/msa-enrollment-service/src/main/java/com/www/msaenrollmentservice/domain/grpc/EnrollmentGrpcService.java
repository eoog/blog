package com.www.msaenrollmentservice.domain.grpc;

import com.example.msaenrollmentservice.domain.service.EnrollmentServiceGrpc;
import com.example.msaenrollmentservice.domain.service.EnrollmentServiceOuterClass;
import com.www.msaenrollmentservice.domain.entity.Enrollment;
import com.www.msaenrollmentservice.domain.entity.Subscription;
import com.www.msaenrollmentservice.domain.service.EnrollmentService;
import io.grpc.stub.StreamObserver;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class EnrollmentGrpcService extends EnrollmentServiceGrpc.EnrollmentServiceImplBase {

  private final EnrollmentService enrollmentService;

  public EnrollmentGrpcService(EnrollmentService enrollmentService) {
    this.enrollmentService = enrollmentService;
  }

  @Override
  public void registerCourse(EnrollmentServiceOuterClass.CourseRegistrationRequest request,
      StreamObserver<EnrollmentServiceOuterClass.CourseRegistrationResponse> responseObserver) {
    try {
      Enrollment enrollment = enrollmentService.registerCourse(request.getUserId(),
          request.getCourseId(), request.getPaymentId());
      EnrollmentServiceOuterClass.CourseRegistrationResponse response = EnrollmentServiceOuterClass.CourseRegistrationResponse.newBuilder()
          .setCourseId(enrollment.getCourseId())
          .setUserId(enrollment.getUserId())
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      log.error("registerCourse error : ", e);
      responseObserver.onError(e);
    }
  }

  @Override
  public void manageSubscription(EnrollmentServiceOuterClass.SubscriptionRequest request,
      StreamObserver<EnrollmentServiceOuterClass.SubscriptionResponse> responseObserver) {
    try {
      LocalDateTime startDate = LocalDateTime.ofInstant(
          Instant.ofEpochMilli(request.getStartDate()), ZoneId.systemDefault());
      LocalDateTime endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getEndDate()),
          ZoneId.systemDefault());

      Subscription subscription = enrollmentService.manageSubscription(
          request.getUserId(),
          startDate,
          endDate,
          request.getPaymentId());
      EnrollmentServiceOuterClass.SubscriptionResponse response = EnrollmentServiceOuterClass.SubscriptionResponse.newBuilder()
          .setSubscription(subscription.toProto())
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      log.error("manageSubscription error : ", e);
      responseObserver.onError(e);
    }
  }

  @Override
  public void renewSubscription(EnrollmentServiceOuterClass.RenewSubscriptionRequest request,
      StreamObserver<EnrollmentServiceOuterClass.SubscriptionResponse> responseObserver) {
    try {
      LocalDateTime startDate = LocalDateTime.ofInstant(
          Instant.ofEpochMilli(request.getStartDate()), ZoneId.systemDefault());
      LocalDateTime endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(request.getEndDate()),
          ZoneId.systemDefault());

      Subscription subscription = enrollmentService.renewSubscription(
          request.getSubscriptionId(),
          startDate,
          endDate);
      EnrollmentServiceOuterClass.SubscriptionResponse response = EnrollmentServiceOuterClass.SubscriptionResponse.newBuilder()
          .setSubscription(subscription.toProto())
          .build();
      responseObserver.onNext(response);
      responseObserver.onCompleted();
    } catch (Exception e) {
      log.error("renewSubscription error : ", e);
      responseObserver.onError(e);
    }
  }

  @Override
  public void checkCourseAccess(EnrollmentServiceOuterClass.CourseAccessRequest request,
      StreamObserver<EnrollmentServiceOuterClass.CourseAccessResponse> responseObserver) {
    boolean hasAccess = enrollmentService.checkCourseAccess(request.getUserId(),
        request.getCourseId());
    EnrollmentServiceOuterClass.CourseAccessResponse response = EnrollmentServiceOuterClass.CourseAccessResponse.newBuilder()
        .setHasAccess(hasAccess)
        .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void checkSubscriptionAccess(EnrollmentServiceOuterClass.SubscriptionAccessRequest request,
      StreamObserver<EnrollmentServiceOuterClass.SubscriptionAccessResponse> responseObserver) {
    boolean hasAccess = enrollmentService.checkSubscriptionAccess(request.getUserId(),
        LocalDateTime.now());
    EnrollmentServiceOuterClass.SubscriptionAccessResponse response = EnrollmentServiceOuterClass.SubscriptionAccessResponse.newBuilder()
        .setHasAccess(hasAccess)
        .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getUserEnrollments(EnrollmentServiceOuterClass.UserEnrollmentsRequest request,
      StreamObserver<EnrollmentServiceOuterClass.UserEnrollmentsResponse> responseObserver) {
    List<Enrollment> enrollments = enrollmentService.getUserEnrollments(request.getUserId());
    EnrollmentServiceOuterClass.UserEnrollmentsResponse.Builder responseBuilder = EnrollmentServiceOuterClass.UserEnrollmentsResponse.newBuilder();

    for (Enrollment enrollment : enrollments) {
      responseBuilder.addEnrollments(enrollment.toProto());
    }

    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }

  @Override
  public void getUserPlanSubscriptions(EnrollmentServiceOuterClass.UserSubscriptionsRequest request,
      StreamObserver<EnrollmentServiceOuterClass.UserSubscriptionsResponse> responseObserver) {
    List<Subscription> subscriptions = enrollmentService.getUserPlanSubscriptions(
        request.getUserId());
    EnrollmentServiceOuterClass.UserSubscriptionsResponse.Builder responseBuilder = EnrollmentServiceOuterClass.UserSubscriptionsResponse.newBuilder();

    for (Subscription subscription : subscriptions) {
      responseBuilder.addSubscriptions(subscription.toProto());
    }
    responseObserver.onNext(responseBuilder.build());
    responseObserver.onCompleted();
  }
}
