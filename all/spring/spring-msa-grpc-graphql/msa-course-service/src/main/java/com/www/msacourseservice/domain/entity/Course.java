package com.www.msacourseservice.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 강의에 관한 테이블
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "courses")
public class Course {

  // 강의 고유 식별키
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "course_id")
  private Long id;

  // 강의 제목
  @Column(nullable = false)
  private String title;

  // 강의에 대한 자세한 설명
  @Column
  private String description;

  // 강사의 식별자 , 외래키로 사용될 수 있음
  @Column(name = "instructor_id", nullable = false)
  private Long instructorId;

  // 강의 생성 시간
  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @OneToMany(mappedBy = "course")
  private List<CourseSession> sessions;

  @OneToMany(mappedBy = "course")
  private List<CourseRating> ratings;

  public Course(Long courseId) {
    this.id = courseId;
  }
}
