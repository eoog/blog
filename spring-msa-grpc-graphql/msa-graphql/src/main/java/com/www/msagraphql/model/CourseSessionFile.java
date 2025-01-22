package com.www.msagraphql.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSessionFile implements Serializable {
  private Long fileId;
  private Long courseSessionId;
  private CourseSession courseSession;
  private String fileName;
  private String fileType;
  private String filePath;
}
