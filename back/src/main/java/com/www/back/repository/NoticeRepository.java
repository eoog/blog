package com.www.back.repository;

import com.www.back.entity.Notice;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

  @Query("SELECT n FROM Notice n WHERE n.createdDate >= :startDate ORDER BY n.createdDate")
  List<Notice> findByCreatedDate(@Param("startDate") LocalDateTime startDate);

}
