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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        review = new Review(1L, 1L, 1L, "Great course!", 5);
        course = CourseEntity.builder().id(1L).title("Java").build();
        user = UserEntity.builder().id(1L).username("john").build();
    }

    @Test
    void testCreateReview() {
        when(courseRepository.getCourseById(1L)).thenReturn(Optional.of(course));
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.createReview(any())).thenAnswer(i -> i.getArgument(0));
        Review result = reviewService.createReview(review);
        assertEquals("Great course!", result.getReviewText());
    }

    @Test
    void testDeleteReview_Success() {
        when(reviewRepository.deleteReview(1L)).thenReturn(true);
        assertTrue(reviewService.deleteReview(1L));
    }

    @Test
    void testGetAllReviews() {
        when(reviewRepository.getAllReviews()).thenReturn(List.of());
        assertNotNull(reviewService.getAllReviews());
    }
}
