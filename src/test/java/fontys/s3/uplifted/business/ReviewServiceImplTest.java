package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.ReviewServiceImpl;
import fontys.s3.uplifted.domain.Review;
import fontys.s3.uplifted.persistence.*;
import fontys.s3.uplifted.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplTest {

    @Mock private ReviewRepository reviewRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private ReviewServiceImpl reviewService;

    private Review review;
    private CourseEntity course;
    private UserEntity user;
    private ReviewEntity reviewEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        course = CourseEntity.builder().id(1L).title("Java").build();
        user = UserEntity.builder().id(1L).username("john").build();

        review = new Review(1L, 1L, 1L, "Great course!", 5);
        reviewEntity = ReviewEntity.builder()
                .id(1L)
                .reviewText("Great course!")
                .rating(5)
                .course(course)
                .user(user)
                .build();
    }

    @Test
    void testCreateReview() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.save(any())).thenReturn(reviewEntity);

        Review result = reviewService.createReview(review);
        assertEquals("Great course!", result.getReviewText());
        verify(reviewRepository, times(1)).save(any());
    }

    @Test
    void testDeleteReview_Success() {
        when(reviewRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reviewRepository).deleteById(1L);

        assertTrue(reviewService.deleteReview(1L));
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllReviews() {
        when(reviewRepository.findAll()).thenReturn(List.of(reviewEntity));
        List<Review> result = reviewService.getAllReviews();

        assertEquals(1, result.size());
        assertEquals("Great course!", result.get(0).getReviewText());
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    void testUpdateReview_Success() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(reviewEntity));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.save(any())).thenReturn(reviewEntity);

        Optional<Review> result = reviewService.updateReview(1L, review);
        assertTrue(result.isPresent());
        assertEquals("Great course!", result.get().getReviewText());
    }

    @Test
    void testGetReviewById_Found() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(reviewEntity));
        Optional<Review> result = reviewService.getReviewById(1L);

        assertTrue(result.isPresent());
        assertEquals("Great course!", result.get().getReviewText());
    }
}
