package com.www.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Board {

  // 게시판 식별 id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 게시판 제목
  @Column(nullable = false)
  private String title;

  // 게시판 내용
  @Column(nullable = false)
  private String description;

  // 등록일자
  @CreatedDate
  @Column(insertable = true)
  private LocalDateTime createdDate;

  // 업데이트 일자
  @LastModifiedDate
  private LocalDateTime updatedDate;

  @PrePersist
  protected void onCreate() {
    this.createdDate = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedDate = LocalDateTime.now();
  }

}
