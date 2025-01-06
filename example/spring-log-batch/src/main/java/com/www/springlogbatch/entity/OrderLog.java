package com.www.springlogbatch.entity;

import java.util.List;
import lombok.Data;

@Data
public class OrderLog {
  private String orderId;
  private String userId;
  private List<String> itemList;
  private String orderPrice;
  private String brand;
  private String vendor;
  private String promotionCode;

}
