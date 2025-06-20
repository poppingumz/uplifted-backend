package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.ReviewService;
import fontys.s3.uplifted.business.impl.exception.EntityNotFoundException;
import fontys.s3.uplifted.business.impl.exception.ReviewException;
import fontys.s3.uplifted.business.impl.mapper.ReviewMapper;
import fontys.s3.uplifted.domain.Review;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.ReviewRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.ReviewEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             CourseRepository courseRepository,
                             UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Review> getAllReviews() {
        try {
            return reviewRepository.findAll()
                    .stream()
                    .map(ReviewMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to retrieve all reviews", e);
            throw new ReviewException("Could not retrieve reviews.");
        }
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        try {
            return reviewRepository.findById(id).map(ReviewMapper::toDomain);
        } catch (Exception e) {
            log.error("Failed to retrieve review with ID: {}", id, e);
            throw new ReviewException("Could not find review with ID: " + id);
        }
    }

    @Override
    public Review createReview(Review review) {
        try {
            CourseEntity course = courseRepository.findById(review.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + review.getCourseId()));

            UserEntity user = userRepository.findById(review.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + review.getUserId()));

            ReviewEntity entity = ReviewMapper.toEntity(review, course, user);
            ReviewEntity saved = reviewRepository.save(entity);

            log.info("Review created for course ID: {}, user ID: {}", review.getCourseId(), review.getUserId());

            return ReviewMapper.toDomain(saved);
        } catch (Exception e) {
            log.error("Failed to create review", e);
            throw new ReviewException("Could not create review. Please try again.");
        }
    }

    @Override
    public Optional<Review> updateReview(Long id, Review review) {
        try {
            return reviewRepository.findById(id).map(existing -> {
                CourseEntity course = courseRepository.findById(review.getCourseId())
                        .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + review.getCourseId()));

                UserEntity user = userRepository.findById(review.getUserId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + review.getUserId()));

                ReviewEntity updated = ReviewMapper.toEntity(review, course, user);
                updated.setId(id);

                return ReviewMapper.toDomain(reviewRepository.save(updated));
            });
        } catch (Exception e) {
            log.error("Failed to update review with ID: {}", id, e);
            throw new ReviewException("Could not update review. Please try again.");
        }
    }

    @Override
    public boolean deleteReview(Long id) {
        try {
            if (!reviewRepository.existsById(id)) {
                log.warn("No review found with ID {} to delete", id);
                return false;
            }
            reviewRepository.deleteById(id);
            log.info("Review with ID {} deleted successfully", id);
            return true;
        } catch (Exception e) {
            log.error("Failed to delete review with ID: {}", id, e);
            throw new ReviewException("Could not delete review.");
        }
    }
}
