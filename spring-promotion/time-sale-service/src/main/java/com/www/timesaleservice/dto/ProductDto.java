package com.www.timesaleservice.dto;

import com.www.timesaleservice.domain.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

public class ProductDto {

  @Getter
  @Builder
  public static class CreateReqeust {
    @NotBlank(message = "상품 이름은 필수")
    private String name;
    @NotNull(message = "가격을 필수")
    @Positive(message = "가격은 양수만 가능")
    private Long price;
    @NotBlank(message = "설명은 필수 입니다")
    private String description;
  }

  @Getter
  @Builder
  public static class Response {
    private Long id;
    private String name;
    private Long price;
    private String description;
    private LocalDateTime createdAt;

    public static Response from(Product product) {
      return Response.builder()
          .id(product.getId())
          .name(product.getName())
          .price(product.getPrice())
          .description(product.getDescription())
          .createdAt(product.getCreatedAt())
          .build();
    }
  }
}
