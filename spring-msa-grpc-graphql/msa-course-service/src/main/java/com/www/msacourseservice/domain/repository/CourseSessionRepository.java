package com.www.msacourseservice.domain.repository;

import com.www.msacourseservice.domain.entity.CourseSession;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {

  List<CourseSession> findByCourseId(Long courseId);
}
