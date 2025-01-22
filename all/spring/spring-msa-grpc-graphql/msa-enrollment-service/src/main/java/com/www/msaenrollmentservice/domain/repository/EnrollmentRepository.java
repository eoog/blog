package com.www.msaenrollmentservice.domain.repository;

import com.www.msaenrollmentservice.domain.entity.Enrollment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

  Optional<Enrollment> findByUserIdAndCourseId(long userId, long courseId);

  List<Enrollment> findAllByUserId(long userId);
}
