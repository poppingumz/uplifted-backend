package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.ReviewService;
import fontys.s3.uplifted.domain.Review;
import fontys.s3.uplifted.persistence.ReviewRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.ReviewEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private static final Logger logger = Logger.getLogger(ReviewService.class.getName());

    public ReviewServiceImpl(ReviewRepository reviewRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.getAllReviews()
                .stream()
                .map(ReviewMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Optional<Review> getReviewById(Long id) {
        Optional<Review> review = reviewRepository.getReviewById(id).map(ReviewMapper::toDomain);
        if (review.isEmpty()) {
            logger.warning("Review not found for ID: " + id);
        }
        return review;
    }

    public Review createReview(Review review) {
        CourseEntity course = courseRepository.getCourseById(review.getCourseId()).orElseThrow();
        UserEntity user = userRepository.getUserById(review.getUserId()).orElseThrow();

        ReviewEntity entity = ReviewMapper.toEntity(review, course, user);
        ReviewEntity savedEntity = reviewRepository.createReview(entity);
        return ReviewMapper.toDomain(savedEntity);
    }


    public Optional<Review> updateReview(Long id, Review review) {
        CourseEntity course = courseRepository.getCourseById(review.getCourseId()).orElseThrow();
        UserEntity user = userRepository.getUserById(review.getUserId()).orElseThrow();

        ReviewEntity entity = ReviewMapper.toEntity(review, course, user);
        return reviewRepository.updateReview(id, entity).map(ReviewMapper::toDomain);
    }

    public boolean deleteReview(Long id) {
        return reviewRepository.deleteReview(id);
    }
}
