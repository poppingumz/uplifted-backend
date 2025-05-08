package fontys.s3.uplifted.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.s3.uplifted.business.QuizService;
import fontys.s3.uplifted.domain.Quiz;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuizController.class)
@Import(fontys.s3.uplifted.config.TestSecurityConfig.class)
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;

    @Autowired
    private ObjectMapper objectMapper;

    private Quiz quiz;

    @BeforeEach
    void setup() {
        quiz = Quiz.builder()
                .id(10L)
                .title("Test Quiz")
                .courseId(1L)
                .description("Sample description")
                .build();
    }

    @Test
    void testCreateQuizSuccess() throws Exception {
        when(quizService.createQuiz(any())).thenReturn(quiz);

        mockMvc.perform(post("/api/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quiz)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Test Quiz"));
    }

    @Test
    void testGetQuizByIdFound() throws Exception {
        when(quizService.getQuizById(10L)).thenReturn(Optional.of(quiz));

        mockMvc.perform(get("/api/quizzes/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Quiz"));
    }

    @Test
    void testGetQuizByIdNotFound() throws Exception {
        when(quizService.getQuizById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/quizzes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetQuizzesByCourseId() throws Exception {
        when(quizService.getQuizzesByCourseId(1L)).thenReturn(List.of(quiz));

        mockMvc.perform(get("/api/quizzes/course/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateQuizSuccess() throws Exception {
        Quiz updatedQuiz = Quiz.builder()
                .id(10L)
                .title("Updated Quiz")
                .courseId(1L)
                .description("Updated")
                .build();

        when(quizService.updateQuiz(eq(10L), any())).thenReturn(updatedQuiz);

        mockMvc.perform(put("/api/quizzes/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedQuiz)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Quiz"));
    }

    @Test
    void testUpdateQuizNotFound() throws Exception {
        when(quizService.updateQuiz(eq(999L), any())).thenReturn(null);

        mockMvc.perform(put("/api/quizzes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quiz)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteQuizSuccess() throws Exception {
        doNothing().when(quizService).deleteQuiz(10L);

        mockMvc.perform(delete("/api/quizzes/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateQuizThrowsException() throws Exception {
        when(quizService.createQuiz(any())).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/api/quizzes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quiz)))
                .andExpect(status().isInternalServerError());
    }
}
