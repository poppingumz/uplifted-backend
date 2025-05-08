package fontys.s3.uplifted.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.s3.uplifted.business.ReviewService;
import fontys.s3.uplifted.config.TestSecurityConfig;
import fontys.s3.uplifted.domain.Review;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
@Import(TestSecurityConfig.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllReviews() throws Exception {
        Review review = new Review(1L, 1L, 2L, "Great course!", 5);
        Mockito.when(reviewService.getAllReviews()).thenReturn(List.of(review));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewText").value("Great course!"));
    }

    @Test
    void testGetReviewById_Found() throws Exception {
        Review review = new Review(1L, 1L, 2L, "Good!", 4);
        Mockito.when(reviewService.getReviewById(1L)).thenReturn(Optional.of(review));

        mockMvc.perform(get("/api/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void testGetReviewById_NotFound() throws Exception {
        Mockito.when(reviewService.getReviewById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reviews/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateReview() throws Exception {
        Review review = new Review(null, 1L, 2L, "Awesome", 5);
        Review saved = new Review(1L, 1L, 2L, "Awesome", 5);

        Mockito.when(reviewService.createReview(any())).thenReturn(saved);

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateReview_Found() throws Exception {
        Review updated = new Review(1L, 1L, 2L, "Updated", 4);
        Mockito.when(reviewService.updateReview(eq(1L), any())).thenReturn(Optional.of(updated));

        mockMvc.perform(put("/api/reviews/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewText").value("Updated"));
    }

    @Test
    void testUpdateReview_NotFound() throws Exception {
        Review review = new Review(null, 1L, 2L, "Updated", 4);
        Mockito.when(reviewService.updateReview(eq(99L), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/reviews/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteReview_Found() throws Exception {
        Mockito.when(reviewService.deleteReview(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/reviews/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteReview_NotFound() throws Exception {
        Mockito.when(reviewService.deleteReview(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/reviews/99"))
                .andExpect(status().isNotFound());
    }
}