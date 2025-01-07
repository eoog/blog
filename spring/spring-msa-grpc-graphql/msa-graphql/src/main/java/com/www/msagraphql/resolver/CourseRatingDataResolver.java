package com.www.msagraphql.resolver;

import com.www.msagraphql.model.Course;
import com.www.msagraphql.model.CourseRating;
import com.www.msagraphql.model.User;
import com.www.msagraphql.service.CourseService;
import com.www.msagraphql.service.UserService;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CourseRatingDataResolver {

  private CourseService courseService;
  private UserService userService;

  public CourseRatingDataResolver(CourseService courseService, UserService userService) {
    this.courseService = courseService;
    this.userService = userService;
  }

  /**
   * CourseRating type > course
   */
  @SchemaMapping(typeName = "CourseRating", field = "course")
  public Course getCourse(CourseRating courseRating) {
    return courseService.findCourseById(courseRating.getCourseId()).orElseThrow();
  }

  /**
   * CourseRating type > user
   */
  @SchemaMapping(typeName = "CourseRating", field = "user")
  public User getUser(User user) {
    return userService.findById(user.getId()).orElseThrow();
  }
}
