package com.www.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article {

  // 게시글 식별 id
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 게시글 제목
  @Column(nullable = false)
  private String title;

  // 게시글 내용
  @Lob // 양이 많아서 clob 으로 저장
  @Column(nullable = false)
  private String content;

  // 작성자
  @ManyToOne
  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private User author;

  // 게시판
  @ManyToOne
  @JsonIgnore
  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private Board board;

  // 댓글
  @OneToMany
  @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
  private List<Comment> comments = new ArrayList<>();

  // 삭제여부
  @Column(nullable = false)
  private Boolean isDeleted = false;

  @CreatedDate
  @Column(insertable = true)
  private LocalDateTime createdDate;

  @LastModifiedDate
  private LocalDateTime updatedDate;

  @Column(nullable = false)
  private Long viewCount = 0L;

  @PrePersist
  protected void onCreate() {
    this.createdDate = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedDate = LocalDateTime.now();
  }
}
