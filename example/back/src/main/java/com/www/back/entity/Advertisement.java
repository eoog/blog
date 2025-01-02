package com.www.back.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.io.Serializable;
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
public class Advertisement implements Serializable { // 직렬화

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title; // 광고 제목

  @Lob // 저장될 타입을 지정해준다 긴거는 이걸로 ㅓ야함
  @Column(nullable = false)
  private String content = ""; // 광고 내용

  @Column(nullable = false)
  private Boolean isDeleted = false; // 삭제 여부

  @Column(nullable = false)
  private Boolean isVisible = true; // 클릭 여부

  @Column(nullable = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startDate; // 시작일

  @Column(nullable = true)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime endDate; // 마감일

  @Column(nullable = false)
  private Integer viewCount = 0; // 방문 횟수

  @Column(nullable = false)
  private Integer clickCount = 0; // 클릭 횟수

  @CreatedDate
  @Column(insertable = true)
  private LocalDateTime createdDate; // 등록일

  @LastModifiedDate
  private LocalDateTime updatedDate; // 수정일

  @PrePersist
  protected void onCreate() {
    this.createdDate = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedDate = LocalDateTime.now();
  }
}
