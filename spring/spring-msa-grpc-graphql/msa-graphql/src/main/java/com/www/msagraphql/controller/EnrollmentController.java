package com.www.msagraphql.controller;

import com.www.msagraphql.model.Enrollment;
import com.www.msagraphql.model.Payment;
import com.www.msagraphql.model.PlanSubscription;
import com.www.msagraphql.service.EnrollmentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EnrollmentController {

  private final EnrollmentService enrollmentService;

  @Autowired
  public EnrollmentController(EnrollmentService enrollmentService) {
    this.enrollmentService = enrollmentService;
  }

  /**
   * 특정 사용자의 모든 등록 정보를 조회합니다.
   */

  @QueryMapping
  public List<Enrollment> getUserEnrollments(@Argument Long userId) {
    return enrollmentService.getEnrollmentsByUserId(userId);
  }

  /**
   * 특정 사용자의 모든 구독 정보를 조회합니다.
   */

  @QueryMapping
  public List<PlanSubscription> getUserPlanSubscriptions(@Argument Long userId) {
    return enrollmentService.getSubscriptionsByUserId(userId);
  }

  /**
   * 강의 이용권 구매
   */

  @MutationMapping
  public Payment purchaseCourse(@Argument Long userId, @Argument Long courseId,
      @Argument Float amount, @Argument String paymentMethod) {
    return enrollmentService.purchaseCourse(userId, courseId, amount, paymentMethod);
  }

  /**
   * # 구독 결제 정보를 생성합니다.
   */

  @MutationMapping
  public Payment purchaseSubscription(@Argument Long userId, @Argument Float amount,
      @Argument String paymentMethod) {
    return enrollmentService.purchaseSubscription(userId, amount, paymentMethod);
  }

  /**
   * # 특정 강의에 대한 권한을 가지고 있는지 여부를 체크 합니다.
   */

  @QueryMapping
  public boolean checkCourseAccess(@Argument Long userId, @Argument Long courseId) {
    // 구독 또는 개별 권한이 있는 경우 허용
    return enrollmentService.checkSubscriptionAccess(userId)
        || enrollmentService.checkCourseAccess(courseId, userId);
  }
}
