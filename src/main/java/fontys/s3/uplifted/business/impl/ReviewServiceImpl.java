package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.ReviewService;
import fontys.s3.uplifted.business.impl.mapper.ReviewMapper;
import fontys.s3.uplifted.domain.Review;
import fontys.s3.uplifted.persistence.ReviewRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.ReviewEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<Review> getAllReviews() {
        try {
            return reviewRepository.getAllReviews()
                    .stream()
                    .map(ReviewMapper::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to retrieve all reviews", e);
            throw new RuntimeException("Could not retrieve reviews.");
        }
    }

    public Optional<Review> getReviewById(Long id) {
        try {
            return reviewRepository.getReviewById(id).map(ReviewMapper::toDomain);
        } catch (Exception e) {
            log.error("Failed to retrieve review with ID: {}", id, e);
            throw new RuntimeException("Could not find review with ID: " + id);
        }
    }

    public Review createReview(Review review) {
        try {
            CourseEntity course = courseRepository.getCourseById(review.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + review.getCourseId()));
            UserEntity user = userRepository.getUserById(review.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + review.getUserId()));

            ReviewEntity entity = ReviewMapper.toEntity(review, course, user);
            ReviewEntity savedEntity = reviewRepository.createReview(entity);
            log.info("Review created for course ID: {}, user ID: {}", review.getCourseId(), review.getUserId());
            return ReviewMapper.toDomain(savedEntity);
        } catch (Exception e) {
            log.error("Failed to create review", e);
            throw new RuntimeException("Could not create review. Please try again.");
        }
    }

    public Optional<Review> updateReview(Long id, Review review) {
        try {
            CourseEntity course = courseRepository.getCourseById(review.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + review.getCourseId()));
            UserEntity user = userRepository.getUserById(review.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + review.getUserId()));

            ReviewEntity entity = ReviewMapper.toEntity(review, course, user);
            return reviewRepository.updateReview(id, entity).map(ReviewMapper::toDomain);
        } catch (Exception e) {
            log.error("Failed to update review with ID: {}", id, e);
            throw new RuntimeException("Could not update review. Please try again.");
        }
    }

    public boolean deleteReview(Long id) {
        try {
            boolean deleted = reviewRepository.deleteReview(id);
            if (deleted) {
                log.info("Review with ID {} deleted successfully", id);
            } else {
                log.warn("No review found with ID {} to delete", id);
            }
            return deleted;
        } catch (Exception e) {
            log.error("Failed to delete review with ID: {}", id, e);
            throw new RuntimeException("Could not delete review.");
        }
    }
}
