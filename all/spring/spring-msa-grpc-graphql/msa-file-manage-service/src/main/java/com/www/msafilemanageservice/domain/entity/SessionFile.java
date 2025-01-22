package com.www.msafilemanageservice.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "session_files")
public class SessionFile {

  // 파일 식별자
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_id")
  private Long fileId;

  // 파일이 속한 세션의 식별자
  @Column(name = "session_id", nullable = false)
  private Long sessionId;

  // 저장된 파일 이름
  @Column(name = "file_name", nullable = false, length = 255)
  private String fileName;

  // 파일의 유형 ( 예 : mp4 )
  @Column(name = "file_type", nullable = false, length = 50)
  private String fileType;

  // 서버 상 파일이 저장된 경로
  @Column(name = "file_path", nullable = false, length = 255)
  private String filePath;

  // 등록일
  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime createdAt = LocalDateTime.now();

  // 수정일
  @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime updatedAt = LocalDateTime.now();

  public SessionFile(Long sessionId, String fileName, String fileType, String filePath) {
    this.sessionId = sessionId;
    this.fileName = fileName;
    this.fileType = fileType;
    this.filePath = filePath;
  }

}
