package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.QuizServiceImpl;
import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.persistence.QuizRepository;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
import fontys.s3.uplifted.business.impl.mapper.QuizMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceImplTest {

    @Mock private QuizRepository quizRepository;
    @InjectMocks private QuizServiceImpl quizService;

    private Quiz quiz;
    private QuizEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<String, String> qna = Map.of(
                "What is Java?", "A programming language",
                "What is Spring Boot?", "Java Framework"
        );

        quiz = Quiz.builder()
                .id(1L)
                .courseId(1L)
                .title("Java Basics")
                .description("Quiz about Java fundamentals")
                .totalMarks(100)
                .passingMarks(60)
                .questionsAndAnswers(qna)
                .build();

        entity = QuizMapper.toEntity(quiz);
    }

    @Test
    void testCreateQuiz() {
        when(quizRepository.save(any())).thenReturn(entity);
        Quiz created = quizService.createQuiz(quiz);
        assertNotNull(created);
        assertEquals("Java Basics", created.getTitle());
    }

    @Test
    void testGetQuizById_Found() {
        when(quizRepository.findById(1L)).thenReturn(Optional.of(entity));
        Optional<Quiz> result = quizService.getQuizById(1L);
        assertTrue(result.isPresent());
        assertEquals("Java Basics", result.get().getTitle());
    }

    @Test
    void testGetQuizById_NotFound() {
        when(quizRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<Quiz> result = quizService.getQuizById(999L);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetQuizzesByCourseId() {
        when(quizRepository.findByCourseId(1L)).thenReturn(List.of(entity));
        List<Quiz> quizzes = quizService.getQuizzesByCourseId(1L);
        assertEquals(1, quizzes.size());
        assertEquals("Java Basics", quizzes.get(0).getTitle());
    }

    @Test
    void testUpdateQuiz_Success() {
        when(quizRepository.update(eq(1L), any())).thenReturn(Optional.of(entity));
        Quiz updated = quizService.updateQuiz(1L, quiz);
        assertNotNull(updated);
        assertEquals("Java Basics", updated.getTitle());
    }

    @Test
    void testDeleteQuiz() {
        doNothing().when(quizRepository).delete(1L);
        quizService.deleteQuiz(1L);
        verify(quizRepository, times(1)).delete(1L);
    }
}
