package com.www.msagraphql.controller;

import com.www.msagraphql.exception.CourseNotFoundException;
import com.www.msagraphql.model.Course;
import com.www.msagraphql.model.CourseRating;
import com.www.msagraphql.model.CourseSession;
import com.www.msagraphql.service.CourseService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class CourseController {

  private final CourseService courseService;

  @Autowired
  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  /**
   * 모든 강의 (Course) 를 조회
   */

  @QueryMapping
  public List<Course> listCourses() {
    return courseService.findAllCourses();
  }

  /**
   * 특정 강의 (Course) 를 조회
   */

  @QueryMapping
  public Course getCourse(@Argument Long userId, @Argument Long courseId) {
    return courseService.findCourseById(courseId)
        .orElseThrow(() -> new CourseNotFoundException("강의를 찾을수 없습니다."));
  }

  ;

  /**
   * 특정 강의의 모든 세션을 조회합니다.
   */

  @QueryMapping
  public List<CourseSession> listCourseSessions(@Argument Long courseId) {
    return courseService.findAllSessionsByCourseId(courseId);
  }

  /**
   * 특정 강의의 모든 세션을 조회합니다.
   */

  @MutationMapping
  public Course createCourse(@Argument String title, @Argument String description,
      @Argument Long instructorId) {
    return courseService.createCourse(title, description, instructorId);
  }

  /**
   * 특정 강의의 정보를 업데이트 합니다.
   */

  @MutationMapping
  public Course updateCourse(@Argument Long id, @Argument String title,
      @Argument String description) {
    return courseService.updateCourse(id, title, description);
  }

  /**
   * 특정 강의 새로운 세션을 추가합니다.
   */

  @MutationMapping
  public CourseSession addCourseSession(@Argument Long courseId, @Argument String title) {
    return courseService.addSessionToCourse(courseId, title);
  }

  /**
   * 특정 강의에 대한 새로운 평가를 추가합니다.
   */
  @MutationMapping
  public CourseRating rateCourse(@Argument Long userId, @Argument Long courseId,
      @Argument Integer rating, @Argument String comment) {
    return courseService.addRatingToCourse(userId, courseId, rating, comment);
  }

}
