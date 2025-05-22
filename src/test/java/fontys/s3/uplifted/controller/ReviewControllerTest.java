package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.ReviewService;
import fontys.s3.uplifted.domain.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController controller;

    private Review sampleReview;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleReview = new Review(1L, 1L, 2L, "Great!", 5);
    }

    @Test
    void shouldGetAllReviews() {
        when(reviewService.getAllReviews()).thenReturn(List.of(sampleReview));

        ResponseEntity<List<Review>> resp = controller.getAllReviews();

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("Great!", resp.getBody().get(0).getReviewText());
        verify(reviewService).getAllReviews();
    }

    @Test
    void shouldGetReviewByIdWhenFound() {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.of(sampleReview));

        ResponseEntity<Review> resp = controller.getReviewById(1L);

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(5, resp.getBody().getRating());
        verify(reviewService).getReviewById(1L);
    }

    @Test
    void shouldReturn404WhenReviewNotFound() {
        when(reviewService.getReviewById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Review> resp = controller.getReviewById(99L);

        assertEquals(404, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(reviewService).getReviewById(99L);
    }

    @Test
    void shouldCreateReview() {
        Review toCreate = new Review(null, 1L, 2L, "Awesome", 5);
        when(reviewService.createReview(toCreate)).thenReturn(sampleReview);

        ResponseEntity<Review> resp = controller.createReview(toCreate);

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(1L, resp.getBody().getId());
        verify(reviewService).createReview(toCreate);
    }

    @Test
    void shouldUpdateReviewWhenFound() {
        Review updated = new Review(1L, 1L, 2L, "Updated", 4);
        when(reviewService.updateReview(eq(1L), any(Review.class)))
                .thenReturn(Optional.of(updated));

        ResponseEntity<Review> resp = controller.updateReview(1L, updated);

        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("Updated", resp.getBody().getReviewText());
        verify(reviewService).updateReview(1L, updated);
    }

    @Test
    void shouldReturn404WhenUpdateReviewNotFound() {
        Review updated = new Review(null, 1L, 2L, "Updated", 4);
        when(reviewService.updateReview(eq(99L), any(Review.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<Review> resp = controller.updateReview(99L, updated);

        assertEquals(404, resp.getStatusCodeValue());
        assertNull(resp.getBody());
        verify(reviewService).updateReview(99L, updated);
    }

    @Test
    void shouldDeleteReviewWhenFound() {
        when(reviewService.deleteReview(1L)).thenReturn(true);

        ResponseEntity<Void> resp = controller.deleteReview(1L);

        assertEquals(204, resp.getStatusCodeValue());
        verify(reviewService).deleteReview(1L);
    }

    @Test
    void shouldReturn404WhenDeleteReviewNotFound() {
        when(reviewService.deleteReview(99L)).thenReturn(false);

        ResponseEntity<Void> resp = controller.deleteReview(99L);

        assertEquals(404, resp.getStatusCodeValue());
        verify(reviewService).deleteReview(99L);
    }
}
