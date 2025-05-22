package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.QuizService;
import fontys.s3.uplifted.domain.Quiz;
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

class QuizControllerTest {

    @Mock
    private QuizService quizService;

    @InjectMocks
    private QuizController quizController;

    private Quiz quiz;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        quiz = Quiz.builder()
                .id(10L)
                .title("Test Quiz")
                .courseId(1L)
                .description("Sample description")
                .build();
    }

    @Test
    void shouldCreateQuizSuccessfully() {
        when(quizService.createQuiz(any(Quiz.class))).thenReturn(quiz);

        ResponseEntity<Quiz> response = quizController.createQuiz(quiz);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(quiz, response.getBody());
        verify(quizService, times(1)).createQuiz(quiz);
    }

    @Test
    void shouldReturn500WhenCreateQuizThrows() {
        when(quizService.createQuiz(any(Quiz.class))).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<Quiz> response = quizController.createQuiz(quiz);

        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(quizService, times(1)).createQuiz(quiz);
    }

    @Test
    void shouldReturnQuizWhenFoundById() {
        when(quizService.getQuizById(10L)).thenReturn(Optional.of(quiz));

        ResponseEntity<Quiz> response = quizController.getQuizById(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(quiz, response.getBody());
        verify(quizService, times(1)).getQuizById(10L);
    }

    @Test
    void shouldReturn404WhenQuizNotFoundById() {
        when(quizService.getQuizById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Quiz> response = quizController.getQuizById(99L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(quizService, times(1)).getQuizById(99L);
    }

    @Test
    void shouldReturnQuizzesByCourseId() {
        when(quizService.getQuizzesByCourseId(1L)).thenReturn(List.of(quiz));

        ResponseEntity<List<Quiz>> response = quizController.getQuizzesByCourseId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(quiz, response.getBody().get(0));
        verify(quizService, times(1)).getQuizzesByCourseId(1L);
    }

    @Test
    void shouldUpdateQuizSuccessfully() {
        Quiz updated = Quiz.builder()
                .id(10L)
                .title("Updated Quiz")
                .courseId(1L)
                .description("Updated")
                .build();
        when(quizService.updateQuiz(eq(10L), any(Quiz.class))).thenReturn(updated);

        ResponseEntity<Quiz> response = quizController.updateQuiz(10L, updated);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updated, response.getBody());
        verify(quizService, times(1)).updateQuiz(10L, updated);
    }

    @Test
    void shouldReturn404WhenUpdatingNonexistentQuiz() {
        when(quizService.updateQuiz(eq(999L), any(Quiz.class))).thenReturn(null);

        ResponseEntity<Quiz> response = quizController.updateQuiz(999L, quiz);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(quizService, times(1)).updateQuiz(999L, quiz);
    }

    @Test
    void shouldDeleteQuizSuccessfully() {
        // quizService.deleteQuiz returns void, so just do nothing()
        doNothing().when(quizService).deleteQuiz(10L);

        ResponseEntity<Void> response = quizController.deleteQuiz(10L);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(quizService, times(1)).deleteQuiz(10L);
    }

    @Test
    void shouldReturn500WhenDeleteQuizThrows() {
        doThrow(new RuntimeException("DB error")).when(quizService).deleteQuiz(10L);

        ResponseEntity<Void> response = quizController.deleteQuiz(10L);

        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(quizService, times(1)).deleteQuiz(10L);
    }
}
