package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.StudentAnswerService;
import fontys.s3.uplifted.controller.MentorReviewController.ReviewRequest;
import fontys.s3.uplifted.domain.StudentAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MentorReviewControllerTest {

    @Mock
    private StudentAnswerService answerService;

    @InjectMocks
    private MentorReviewController controller;

    private StudentAnswer pendingAnswer;
    private ReviewRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pendingAnswer = StudentAnswer.builder()
                .id(1L)
                .userId(101L)
                .quizId(201L)
                .submittedAnswer("My answer")
                .awardedMarks(null)
                .mentorFeedback(null)
                .build();

        request = new ReviewRequest();
        request.setMarks(85);
        request.setFeedback("Good job");
    }

    @Test
    void shouldGetPendingReviews() {
        when(answerService.getPendingReviews()).thenReturn(List.of(pendingAnswer));

        ResponseEntity<List<StudentAnswer>> response = controller.getPendingReviews();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("My answer", response.getBody().get(0).getSubmittedAnswer());
        verify(answerService).getPendingReviews();
    }

    @Test
    void shouldReviewAnswerSuccessfully() {
        doNothing().when(answerService).reviewAnswer(1L, 85, "Good job");

        ResponseEntity<?> response = controller.reviewAnswer(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        verify(answerService).reviewAnswer(1L, 85, "Good job");
    }

    @Test
    void shouldReturn500OnReviewError() {
        doThrow(new RuntimeException("DB failure"))
                .when(answerService).reviewAnswer(eq(99L), anyInt(), anyString());

        ResponseEntity<?> response = controller.reviewAnswer(99L, request);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Registration failed: DB failure", response.getBody());
        verify(answerService).reviewAnswer(99L, 85, "Good job");
    }
}
