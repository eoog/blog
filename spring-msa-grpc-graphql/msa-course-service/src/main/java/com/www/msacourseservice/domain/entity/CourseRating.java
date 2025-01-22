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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 강의 평가 테이블
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "course_ratings")
public class CourseRating {

  // 강의 평가 식별자
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rating_id")
  private Long id;

  // 평가가 속한 강의 Id, COURSES 테이블 참조
  @ManyToOne
  @JoinColumn(name = "course_id", nullable = false)
  @JsonBackReference
  private Course course;

  // 평가를 남긴 사용자 ID
  @Column(name = "user_id", nullable = false)
  private Long userId;

  // 사용자가 부여한 평점 1-5점
  @Column(nullable = false)
  private int rating;

  // 사용자가 탐긴 코멘트
  @Column
  private String comment;

  // 생성시간
  @Column(name = "created_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;
}