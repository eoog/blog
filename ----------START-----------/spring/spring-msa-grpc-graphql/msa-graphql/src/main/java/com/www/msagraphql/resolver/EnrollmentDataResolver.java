package com.www.msagraphql.resolver;

import com.www.msagraphql.model.Course;
import com.www.msagraphql.model.Enrollment;
import com.www.msagraphql.model.Payment;
import com.www.msagraphql.model.User;
import com.www.msagraphql.service.CourseService;
import com.www.msagraphql.service.EnrollmentService;
import com.www.msagraphql.service.UserService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
public class EnrollmentDataResolver {

  private final UserService userService;
  private final CourseService courseService;
  private final EnrollmentService enrollmentService;

  @Autowired
  public EnrollmentDataResolver(UserService userService, CourseService courseService,
      EnrollmentService enrollmentService, BatchLoaderRegistry registry) {
    this.userService = userService;
    this.courseService = courseService;
    this.enrollmentService = enrollmentService;

    // 데이터로드 batch 방식으로
    registry.forTypePair(Long.class, Course.class).registerMappedBatchLoader(
        (courseIds, env) -> {
          List<Long> ids = courseIds.stream().toList();
          log.info("---getCourse {}---", ids);
          return Mono.justOrEmpty(courseService.findCoursesByIds(ids)
              .stream()
              .collect(Collectors.toMap(Course::getId, course -> course))
          );
        }
    );
  }

  @SchemaMapping(typeName = "Enrollment", field = "user")
  public User getUser(Enrollment enrollment) {
    return userService.findById(enrollment.getUserId()).orElse(null);
  }

//    @SchemaMapping(typeName = "Enrollment", field = "course")
//    public Course getCourse(Enrollment enrollment) {
//        log.info("---getCourse {}---", enrollment.getCourseId());
//        return courseService.findCourseById(enrollment.getCourseId()).orElse(null);
//    }

  @SchemaMapping(typeName = "Enrollment", field = "course")
  public CompletableFuture<Course> getCourse(Enrollment enrollment,
      DataLoader<Long, Course> loader) {
    return loader.load(enrollment.getCourseId());
  }

  @SchemaMapping(typeName = "Enrollment", field = "payment")
  public Payment getPayment(Enrollment enrollment) {
    return enrollmentService.findPaymentById(enrollment.getPaymentId());
  }
}
