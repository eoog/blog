package com.www.msagraphql.resolver;

import com.www.msagraphql.model.Payment;
import com.www.msagraphql.model.PlanSubscription;
import com.www.msagraphql.model.User;
import com.www.msagraphql.service.EnrollmentService;
import com.www.msagraphql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PlanSubscriptionDataResolver {

  private final UserService userService;
  private final EnrollmentService enrollmentService;

  @Autowired
  public PlanSubscriptionDataResolver(UserService userService,
      EnrollmentService enrollmentService) {
    this.userService = userService;
    this.enrollmentService = enrollmentService;
  }

  @SchemaMapping(typeName = "PlanSubscription", field = "user")
  public User getUser(PlanSubscription subscription) {
    return userService.findById(subscription.getUserId()).orElse(null);
  }

  @SchemaMapping(typeName = "PlanSubscription", field = "payment")
  public Payment getPayment(PlanSubscription subscription) {
    return enrollmentService.findPaymentById(subscription.getPaymentId());
  }
}
