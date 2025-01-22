package com.www.msacourseservice.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 강의 세션 테이블
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "course_sessions")
public class CourseSession {
  
  // 세션 식별자
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "session_id")
  private Long id;

  // 해당 세션이 속한 강의 제목
  @Column(nullable = false)
  private String title;

  // 해당 세션이 속한 강의의 ID, COURSES 테이블 참조
  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  @JsonBackReference
  private Course course;
}
