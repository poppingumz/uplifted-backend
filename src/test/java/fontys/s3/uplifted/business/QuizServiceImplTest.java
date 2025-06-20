package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.QuizServiceImpl;
import fontys.s3.uplifted.business.impl.exception.EntityNotFoundException;
import fontys.s3.uplifted.business.impl.exception.QuizException;
import fontys.s3.uplifted.business.impl.exception.UnauthorizedAccessException;
import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.domain.Question;
import fontys.s3.uplifted.domain.Answer;
import fontys.s3.uplifted.domain.enums.QuestionType;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.QuizRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceImplTest {

    private QuizRepository quizRepository;
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private QuizServiceImpl quizService;

    @BeforeEach
    void setUp() {
        quizRepository = mock(QuizRepository.class);
        courseRepository = mock(CourseRepository.class);
        userRepository = mock(UserRepository.class);
        quizService = new QuizServiceImpl(quizRepository, courseRepository, userRepository);
    }

    @Test
    void createQuiz_success() {
        Quiz quiz = Quiz.builder()
                .title("Test Quiz")
                .createdById(1L)
                .courseId(2L)
                .questions(List.of(Question.builder()
                        .text("Q1")
                        .type(QuestionType.MULTIPLE_CHOICE)
                        .answers(List.of(Answer.builder().text("A").correct(true).build()))
                        .build()))
                .build();

        CourseEntity course = CourseEntity.builder().id(2L).build();
        UserEntity creator = UserEntity.builder().id(1L).build();

        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(userRepository.findById(1L)).thenReturn(Optional.of(creator));
        when(quizRepository.save(any())).thenAnswer(invocation -> {
            QuizEntity saved = invocation.getArgument(0);
            saved.setId(99L);
            return saved;
        });

        Quiz result = quizService.createQuiz(quiz);
        assertNotNull(result);
    }

    @Test
    void createQuiz_missingCourse_throwsQuizException() {
        Quiz quiz = Quiz.builder().title("No Course").createdById(1L).courseId(5L).build();
        when(courseRepository.findById(5L)).thenThrow(new EntityNotFoundException("Course not found"));
        assertThrows(QuizException.class, () -> quizService.createQuiz(quiz));
    }

    @Test
    void getQuizById_success() {
        UserEntity creator = UserEntity.builder().id(1L).build();
        QuizEntity entity = QuizEntity.builder()
                .id(10L)
                .title("Test")
                .createdBy(creator)
                .build();

        when(quizRepository.findById(10L)).thenReturn(Optional.of(entity));

        Optional<Quiz> result = quizService.getQuizById(10L);
        assertTrue(result.isPresent());
        assertEquals("Test", result.get().getTitle());
        assertEquals(1L, result.get().getCreatedById());
    }

    @Test
    void getQuizzesByCourseId_success() {
        UserEntity creator = UserEntity.builder().id(1L).build();
        QuizEntity quizEntity = QuizEntity.builder()
                .id(1L)
                .title("A")
                .createdBy(creator)
                .build();

        when(quizRepository.findByCourseId(1L)).thenReturn(List.of(quizEntity));

        List<Quiz> quizzes = quizService.getQuizzesByCourseId(1L);

        assertEquals(1, quizzes.size());
        assertEquals("A", quizzes.get(0).getTitle());
        assertEquals(1L, quizzes.get(0).getCreatedById());
    }

    @Test
    void getQuizzesByCourseId_failure() {
        when(quizRepository.findByCourseId(anyLong())).thenThrow(new RuntimeException("DB down"));
        assertThrows(QuizException.class, () -> quizService.getQuizzesByCourseId(1L));
    }

    @Test
    void updateQuiz_success() {
        Quiz quiz = Quiz.builder().createdById(1L).courseId(2L).title("Updated").build();
        QuizEntity existing = QuizEntity.builder().id(1L).createdBy(UserEntity.builder().id(1L).build()).build();

        when(quizRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(1L)).thenReturn(Optional.of(UserEntity.builder().id(1L).build()));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(CourseEntity.builder().id(2L).build()));
        when(quizRepository.save(any())).thenReturn(existing);

        Quiz result = quizService.updateQuiz(1L, quiz);
        assertNotNull(result);
    }

    @Test
    void updateQuiz_unauthorized() {
        Quiz quiz = Quiz.builder().createdById(99L).build();
        QuizEntity existing = QuizEntity.builder()
                .id(1L)
                .createdBy(UserEntity.builder().id(1L).build())
                .build();

        when(quizRepository.findById(1L)).thenReturn(Optional.of(existing));

        QuizException ex = assertThrows(QuizException.class, () -> quizService.updateQuiz(1L, quiz));
        assertEquals("Could not update quiz.", ex.getMessage());
    }


    @Test
    void updateQuiz_failure_throwsQuizException() {
        Quiz quiz = Quiz.builder().createdById(1L).build();
        when(quizRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));
        assertThrows(QuizException.class, () -> quizService.updateQuiz(1L, quiz));
    }

    @Test
    void deleteQuiz_success() {
        when(quizRepository.existsById(1L)).thenReturn(true);
        quizService.deleteQuiz(1L);
        verify(quizRepository).deleteById(1L);
    }

    @Test
    void deleteQuiz_notFound() {
        when(quizRepository.existsById(99L)).thenReturn(false);
        QuizException exception = assertThrows(QuizException.class, () -> quizService.deleteQuiz(99L));
        assertTrue(exception.getMessage().contains("Could not delete quiz."));
    }


    @Test
    void deleteQuiz_failure_throwsQuizException() {
        when(quizRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("DB error")).when(quizRepository).deleteById(1L);
        assertThrows(QuizException.class, () -> quizService.deleteQuiz(1L));
    }

    @Test
    void getQuizzesByCreatorId_success() {
        UserEntity user = UserEntity.builder().id(1L).build();
        QuizEntity quizEntity = QuizEntity.builder().id(10L).createdBy(user).build();
        when(quizRepository.findByCreatedById(1L)).thenReturn(List.of(quizEntity));

        List<Quiz> result = quizService.getQuizzesByCreatorId(1L);

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getId());
    }
}
