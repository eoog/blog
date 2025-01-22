package com.www.back.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "adViewHistory")
@Getter
@Setter
public class AdViewHistory {

  @Id
  private String id; // 식별자
  private Long adId; // 광고 아이디
  private String username; // 사용자
  private String clientIp; // 아이피
  private Boolean isTrueView = false; // 광고 봤는지
  private LocalDateTime createdDate = LocalDateTime.now(); // 현재 날짜
}
