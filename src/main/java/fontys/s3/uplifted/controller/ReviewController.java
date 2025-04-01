package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.ReviewService;
import fontys.s3.uplifted.domain.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        try {
            return ResponseEntity.ok(reviewService.getAllReviews());
        } catch (Exception e) {
            log.error("Failed to retrieve reviews", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        try {
            return reviewService.getReviewById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("Review with ID {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Error fetching review with ID {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        try {
            Review createdReview = reviewService.createReview(review);
            return ResponseEntity.ok(createdReview);
        } catch (Exception e) {
            log.error("Failed to create review", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody Review review) {
        try {
            return reviewService.updateReview(id, review)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("Review with ID {} not found for update", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Failed to update review with ID {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        try {
            boolean deleted = reviewService.deleteReview(id);
            if (deleted) {
                log.info("Review with ID {} deleted successfully", id);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("Review with ID {} not found for deletion", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Failed to delete review with ID {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
