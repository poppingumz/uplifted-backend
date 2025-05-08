package fontys.s3.uplifted.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.s3.uplifted.business.StudentAnswerService;
import fontys.s3.uplifted.controller.MentorReviewController.ReviewRequest;
import fontys.s3.uplifted.domain.StudentAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MentorReviewController.class)
@Import(fontys.s3.uplifted.config.TestSecurityConfig.class)
public class MentorReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentAnswerService answerService;

    @Autowired
    private ObjectMapper objectMapper;

    private StudentAnswer pendingAnswer;

    @BeforeEach
    void setup() {
        pendingAnswer = StudentAnswer.builder()
                .id(1L)
                .userId(101L)
                .quizId(201L)
                .submittedAnswer("My answer")
                .awardedMarks(null)
                .mentorFeedback(null)
                .build();
    }

    @Test
    void testGetPendingReviewsSuccess() throws Exception {
        when(answerService.getPendingReviews()).thenReturn(List.of(pendingAnswer));

        mockMvc.perform(get("/api/mentor_review/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].submittedAnswer").value("My answer"));
    }

    @Test
    void testReviewAnswerSuccess() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setMarks(85);
        request.setFeedback("Good job");

        doNothing().when(answerService).reviewAnswer(eq(1L), eq(85), eq("Good job"));

        mockMvc.perform(patch("/api/mentor_review/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testReviewAnswerThrowsException() throws Exception {
        ReviewRequest request = new ReviewRequest();
        request.setMarks(40);
        request.setFeedback("Needs improvement");

        doThrow(new RuntimeException("DB failure"))
                .when(answerService).reviewAnswer(anyLong(), eq(40), eq("Needs improvement"));

        mockMvc.perform(patch("/api/mentor_review/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Registration failed: DB failure"));
    }

}
