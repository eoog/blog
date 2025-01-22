package com.www.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyMessage {
  private int id;
  private int age;
  private String name;
  private String content;
}
