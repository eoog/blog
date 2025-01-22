package com.www.back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AdClickStat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long adId;

  private Long count;

  private String dt;

  @CreatedDate
  @Column(insertable = true)
  private LocalDateTime createdDate;

  @PrePersist
  protected void onCreate() {
    this.createdDate = LocalDateTime.now();
  }
}
