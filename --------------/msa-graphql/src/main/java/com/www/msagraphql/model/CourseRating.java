package com.www.msagraphql.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseRating implements Serializable {
  private Long id;
  private Long courseId;
  private Long userId;
  private Integer rating;
  private String comment;
}
