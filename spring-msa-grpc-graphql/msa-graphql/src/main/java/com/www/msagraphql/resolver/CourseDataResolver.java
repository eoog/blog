package com.www.msagraphql.resolver;

import com.www.msagraphql.model.Course;
import com.www.msagraphql.model.CourseSession;
import com.www.msagraphql.model.User;
import com.www.msagraphql.service.CourseService;
import com.www.msagraphql.service.UserService;
import java.util.List;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

/**
 * 조인을 해주는것
 */
@Controller
public class CourseDataResolver {

  private final CourseService courseService;
  private final UserService userService;

  public CourseDataResolver(CourseService courseService, UserService userService) {
    this.courseService = courseService;
    this.userService = userService;
  }

  /**
   * Course type > courseSessions
   */
  @SchemaMapping(typeName = "Course", field = "courseSessions")
  public List<CourseSession> getSessions(Course course) {
    return courseService.findAllSessionsByCourseId(course.getId());
  }

  /**
   * Course type > instructor
   */
  @SchemaMapping(typeName = "Course", field = "instructor")
  public User getInstructor(Course course) {
    return userService.findById(course.getInstructorId()).orElseThrow();
  }

}
