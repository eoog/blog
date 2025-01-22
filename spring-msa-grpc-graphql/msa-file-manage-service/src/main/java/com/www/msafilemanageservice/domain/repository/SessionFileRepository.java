package com.www.msafilemanageservice.domain.repository;

import com.www.msafilemanageservice.domain.entity.SessionFile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionFileRepository extends JpaRepository<SessionFile, Long> {

  Optional<SessionFile> findTopBySessionIdOrderByFileIdDesc(Long sessionId);
}
