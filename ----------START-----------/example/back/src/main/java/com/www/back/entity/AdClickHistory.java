package com.www.back.entity;

import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "adClickHistory") // 몽코 DB , Entity 개념
@Getter
@Setter
public class AdClickHistory {

  @Id
  private String id; // 식별자
  private Long adId; //  광고 번호
  private String username; // 클라이언트
  private String clientIp; // 아이피
  private LocalDateTime createdDate = LocalDateTime.now(); // 현재날짜
}
