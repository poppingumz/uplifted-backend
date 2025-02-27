package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.ReviewEntity;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    List<ReviewEntity> getAllReviews();
    Optional<ReviewEntity> getReviewById(Long id);
    ReviewEntity createReview(ReviewEntity review);
    Optional<ReviewEntity> updateReview(Long id, ReviewEntity review);
    boolean deleteReview(Long id);
}
