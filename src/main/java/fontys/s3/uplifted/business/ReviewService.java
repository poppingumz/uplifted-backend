package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> getAllReviews();
    Optional<Review> getReviewById(Long id);
    Review createReview(Review review);
    Optional<Review> updateReview(Long id, Review review);
    boolean deleteReview(Long id);
}
