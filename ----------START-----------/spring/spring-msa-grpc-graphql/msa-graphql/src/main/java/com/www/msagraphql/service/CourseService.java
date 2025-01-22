package com.www.msagraphql.service;

import com.www.msagraphql.model.Course;
import com.www.msagraphql.model.CourseRating;
import com.www.msagraphql.model.CourseSession;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class CourseService {

  private static final String BASE_URL = "http://msa-course-service/courses";
  private RestTemplate restTemplate;

  @Autowired
  public CourseService(@LoadBalanced RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  /**
   * 새로운 강의를 생성합니다.
   */

  public Course createCourse(String title, String description, Long instructorId) {
    Course course = new Course();
    course.setTitle(title);
    course.setDescription(description);
    course.setInstructorId(instructorId);

    return restTemplate.postForObject(BASE_URL, course, Course.class);
  }

  /**
   * 특정 강의를 업데이트 합니다.
   */
  public Course updateCourse(Long id, String title, String description) {
    Course course = new Course();
    course.setId(id);
    course.setTitle(title);
    course.setDescription(description);

    restTemplate.put(BASE_URL, course, Course.class);

    return course;
  }

  /**
   * 특정 강의에 세션을 추가합니다.
   */

  public CourseSession addSessionToCourse(Long courseId, String title) {
    CourseSession courseSession = new CourseSession();
    courseSession.setTitle(title);

    CourseSession addedSession = restTemplate.postForObject(
        UriComponentsBuilder.fromUriString(BASE_URL + "/{courseId}/sessions")
            .buildAndExpand(courseId).toUri(), courseSession, CourseSession.class);

    if (addedSession != null) {
      addedSession.setCourseId(courseId);
    }

    return addedSession;

  }

  /**
   * 특정 강의에 대한 새로운 평가를 추가합니다.
   */
  public CourseRating addRatingToCourse(Long userId, Long courseId, Integer rating,
      String comment) {
    CourseRating courseRating = new CourseRating();
    courseRating.setUserId(userId);
    courseRating.setCourseId(courseId);
    courseRating.setRating(rating);
    courseRating.setComment(comment);
    String url = UriComponentsBuilder.fromUriString(BASE_URL + "/{courseId}/ratings")
        .buildAndExpand(courseId).toUriString();
    return restTemplate.postForObject(url, courseRating, CourseRating.class);
  }

  /**
   * 모든 강의 course 를 조회
   */

  public List<Course> findAllCourses() {
    Course[] courses = restTemplate.getForObject(BASE_URL, Course[].class);
    if (courses == null) {
      return Collections.emptyList();
    }

    return Arrays.asList(courses);
  }

  /**
   * 특정 강의 course 를 조회
   */

  @Cacheable(value = "course", key = "#courseId")
  public Optional<Course> findCourseById(Long courseId) {
    Course course = null;
    try {
      course = restTemplate.getForObject(BASE_URL + "/" + courseId, Course.class);
    } catch (Exception e) {
      log.error("An error occurred while fetching the course: {}", e.getMessage(), e);
    }

    return Optional.ofNullable(course);
  }

  /**
   * 특정 강의의 모든 세션을 조회합니다.
   */

  public List<CourseSession> findAllSessionsByCourseId(Long courseId) {
    String url = UriComponentsBuilder.fromUriString(BASE_URL + "/{courseId}/sessions")
        .buildAndExpand(courseId).encode(StandardCharsets.UTF_8).toUriString();
    CourseSession[] sessions = restTemplate.getForObject(url, CourseSession[].class);
    if (sessions == null) {
      log.info("zzzzzzzzzzzzzzzzzzzzzz");
      return Collections.emptyList();
    }

    return Arrays.stream(sessions)
        .peek(session -> session.setCourseId(courseId))
        .collect(Collectors.toList());
  }

  @Cacheable(value = "courses", key = "#courseIds")
  public List<Course> findCoursesByIds(List<Long> courseIds) {
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL);
    courseIds.forEach(id -> builder.queryParam("courseId", id));

    URI uri = builder.build().toUri();

    Course[] courses = restTemplate.getForObject(uri, Course[].class);
    if (courses == null) {
      return Collections.emptyList();
    }

    return Arrays.asList(courses);
  }

}
