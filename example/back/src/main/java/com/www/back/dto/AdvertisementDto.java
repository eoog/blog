package com.www.back.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class AdvertisementDto {

  private String title;
  private String content;
  private Boolean isDeleted = false;
  private Boolean isVisible = true;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer viewCount = 0;
  private Integer clickCount = 0;
}
