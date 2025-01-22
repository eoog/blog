package com.www.msafilemanageservice.domain.service;

import com.www.msafilemanageservice.domain.entity.SessionFile;
import com.www.msafilemanageservice.domain.repository.SessionFileRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionFileService {

  private final SessionFileRepository sessionFileRepository;

  // 세션아이디 최상위중 파일네임 오름차순 정렬
  public Optional<SessionFile> findTopBySessionIdOrderByFileIdDesc(Long sessionId) {
    return sessionFileRepository.findTopBySessionIdOrderByFileIdDesc(sessionId);
  }

  // 단일 파일 찾기
  public Optional<SessionFile> findFileById(Long fileId) {
    return sessionFileRepository.findById(fileId);
  }

  // 파일 저장 하기
  public SessionFile saveFile(SessionFile sessionFile) {
    return sessionFileRepository.save(sessionFile);
  }

  // 파일 삭제하기
  public void deleteFile(Long fileId) {
    sessionFileRepository.deleteById(fileId);
  }
}
