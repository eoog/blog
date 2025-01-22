package com.www.msacourseservice.domain.repository;

import com.www.msacourseservice.domain.entity.CourseRating;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {

  List<CourseRating> findByCourseId(Long courseId);
}
