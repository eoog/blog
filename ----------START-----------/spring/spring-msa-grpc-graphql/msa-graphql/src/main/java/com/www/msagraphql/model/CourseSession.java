package com.www.msagraphql.model;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSession implements Serializable {
  private Long id;
  private Long courseId;
  private String title;
  private List<CourseSessionFile> files;
}
