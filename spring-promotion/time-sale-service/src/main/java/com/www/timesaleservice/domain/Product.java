package com.www.timesaleservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String name; // 상품 이름
  
  @Column(nullable = false)
  private Long price; // 상품 가격
  
  @Column(nullable = false)
  private String description; // 상품 설명
  
  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt; // 상품 등록
  
  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime updatedAt; // 상품 수정

  @Builder
  public Product(Long id, String name, Long price, String description) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.description = description;
  }
}
