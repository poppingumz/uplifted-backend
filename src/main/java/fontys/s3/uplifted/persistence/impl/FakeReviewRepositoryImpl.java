package fontys.s3.uplifted.persistence.impl;

import fontys.s3.uplifted.persistence.ReviewRepository;
import fontys.s3.uplifted.persistence.entity.ReviewEntity;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeReviewRepositoryImpl implements ReviewRepository {
    private final Map<Long, ReviewEntity> reviews = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<ReviewEntity> getAllReviews() {
        return new ArrayList<>(reviews.values());
    }

    @Override
    public Optional<ReviewEntity> getReviewById(Long id) {
        return Optional.ofNullable(reviews.get(id));
    }

    @Override
    public ReviewEntity createReview(ReviewEntity review) {
        long newId = idGenerator.getAndIncrement();
        review.setId(newId);
        reviews.put(newId, review);
        return review;
    }

    @Override
    public Optional<ReviewEntity> updateReview(Long id, ReviewEntity updatedReview) {
        if (!reviews.containsKey(id)) {
            return Optional.empty();
        }
        updatedReview.setId(id);
        reviews.put(id, updatedReview);
        return Optional.of(updatedReview);
    }

    @Override
    public boolean deleteReview(Long id) {
        return reviews.remove(id) != null;
    }
}
