package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.QuizServiceImpl;
import fontys.s3.uplifted.business.impl.mapper.QuizMapper;
import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.QuizRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

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
        Quiz quiz = Quiz.builder().courseId(1L).createdById(2L).title("Sample").build();
        CourseEntity courseEntity = CourseEntity.builder().id(1L).build();
        UserEntity userEntity = UserEntity.builder().id(2L).build();
        QuizEntity quizEntity = QuizEntity.builder().id(10L).title("Sample").build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        when(userRepository.findById(2L)).thenReturn(Optional.of(userEntity));
        when(quizRepository.save(any())).thenReturn(quizEntity);

        Quiz result = quizService.createQuiz(quiz);
        assertNotNull(result);
        assertEquals("Sample", result.getTitle());
    }

    @Test
    void getQuizById_notFound() {
        when(quizRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> quizService.getQuizById(99L));
    }
}
