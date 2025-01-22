package com.www.msafilemanageservice.domain.service;

import com.ctc.wstx.util.StringUtil;
import com.www.msafilemanageservice.domain.entity.SessionFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileStorageService {

  @Value("${file.upload-dir}")
  private String uploadDir;

  /**
   * 파일을 서버에 저장하고 파일 정보를 반환합니다.
   * @param file MultipartFile
   * @param sessionId 세션 ID
   * @return 저장된 파일의 메타데이터
   */

  public SessionFile storeFile(MultipartFile file , Long sessionId) {
    String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    String fileName = System.currentTimeMillis() + "_" + originalFileName;

    // 파일이 없음
    try {
      if (fileName.contains("..")) {
        throw new RuntimeException("파일이 저장되어있지 않습니다");
      }

      // 저장된 파일 위치
      Path targetLocation = Paths.get(uploadDir).resolve(fileName);
      // 저장된 파일 위치의 상위폴더 부모펄더 생성하기
      Files.createDirectories(targetLocation.getParent());
      Files.copy(file.getInputStream() , targetLocation , StandardCopyOption.REPLACE_EXISTING);

      return new SessionFile(sessionId,fileName,"mp4",targetLocation.toString());

    } catch (Exception e) {
      throw new RuntimeException("Could not store file \" + fileName + \". Please try again!", e);
    }
  }
}
