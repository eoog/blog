package com.www.msacourseservice.domain.service;

import com.www.msacourseservice.domain.entity.Course;
import com.www.msacourseservice.domain.entity.CourseRating;
import com.www.msacourseservice.domain.repository.CourseRatingRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseRatingService {
  private final CourseRatingRepository ratingRepository;

  @Autowired
  public CourseRatingService(CourseRatingRepository ratingRepository) {
    this.ratingRepository = ratingRepository;
  }

  public CourseRating addRatingToCourse(Long courseId, CourseRating rating) {
    rating.setCourse(new Course(courseId)); // Assuming Course constructor accepts courseId
    return ratingRepository.save(rating);
  }

  public CourseRating updateRating(Long ratingId, CourseRating ratingDetails) {
    CourseRating rating = ratingRepository.findById(ratingId)
        .orElseThrow(() -> new RuntimeException("Rating not found with id " + ratingId));
    rating.setRating(ratingDetails.getRating());
    rating.setComment(ratingDetails.getComment());
    return ratingRepository.save(rating);
  }

  public void deleteRating(Long ratingId) {
    ratingRepository.deleteById(ratingId);
  }

  public List<CourseRating> getAllRatingsByCourseId(Long courseId) {
    return ratingRepository.findByCourseId(courseId);
  }

  public Optional<CourseRating> getRatingById(Long ratingId) {
    return ratingRepository.findById(ratingId);
  }
}
