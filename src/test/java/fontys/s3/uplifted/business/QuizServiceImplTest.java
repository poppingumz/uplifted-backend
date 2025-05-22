package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.QuizServiceImpl;
import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.QuizRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QuizServiceImplTest {

    @Mock QuizRepository quizRepo;
    @Mock CourseRepository courseRepo;
    @Mock UserRepository userRepo;
    @InjectMocks
    QuizServiceImpl service;

    private Quiz quiz;
    private CourseEntity courseEntity;
    private UserEntity userEntity;
    private QuizEntity quizEntity;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        quiz = Quiz.builder()
                .id(null)
                .title("T")
                .description("D")
                .courseId(5L)
                .createdById(7L)
                .build();
        courseEntity = new CourseEntity(); courseEntity.setId(5L);
        userEntity   = new UserEntity();   userEntity.setId(7L);
        quizEntity   = new QuizEntity();
        quizEntity.setId(99L);
        quizEntity.setTitle("T");
        quizEntity.setDescription("D");
        quizEntity.setCourse(courseEntity);
        quizEntity.setCreatedBy(userEntity);
    }

    @Test
    void createQuizSuccess() {
        when(courseRepo.findById(5L)).thenReturn(Optional.of(courseEntity));
        when(userRepo.findById(7L))  .thenReturn(Optional.of(userEntity));
        when(quizRepo.save(any())).thenReturn(quizEntity);

        Quiz created = service.createQuiz(quiz);

        assertEquals(99L, created.getId());
        assertEquals("T", created.getTitle());
        verify(quizRepo).save(ArgumentMatchers.any(QuizEntity.class));
    }

    @Test
    void createQuizCourseNotFound() {
        when(courseRepo.findById(any())).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createQuiz(quiz));
        assertTrue(ex.getMessage().contains("Could not create quiz"));
    }

    @Test
    void createQuizUserNotFound() {
        when(courseRepo.findById(5L)).thenReturn(Optional.of(courseEntity));
        when(userRepo.findById(any())).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.createQuiz(quiz));
        assertTrue(ex.getMessage().contains("Could not create quiz"));
    }

    @Test
    void getQuizByIdFound() {
        when(quizRepo.findById(99L)).thenReturn(Optional.of(quizEntity));
        Optional<Quiz> opt = service.getQuizById(99L);
        assertTrue(opt.isPresent());
        assertEquals(99L, opt.get().getId());
    }

    @Test
    void getQuizByIdEmpty() {
        when(quizRepo.findById(123L)).thenReturn(Optional.empty());
        assertTrue(service.getQuizById(123L).isEmpty());
    }

    @Test
    void getQuizzesByCourseIdSuccess() {
        when(quizRepo.findByCourseId(5L)).thenReturn(List.of(quizEntity));
        List<Quiz> list = service.getQuizzesByCourseId(5L);
        assertEquals(1, list.size());
        assertEquals("T", list.get(0).getTitle());
    }

    @Test
    void getQuizzesByCourseIdError() {
        when(quizRepo.findByCourseId(any()))
                .thenThrow(new RuntimeException("DB"));
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.getQuizzesByCourseId(5L));
        assertTrue(ex.getMessage().contains("Could not load quizzes"));
    }

    @Test
    void updateQuizSuccess() {
        Quiz updated = Quiz.builder()
                .id(null)
                .title("X")
                .description("Y")
                .courseId(5L)
                .createdById(7L)
                .build();
        QuizEntity updatedEntity = new QuizEntity();
        updatedEntity.setId(99L);
        updatedEntity.setTitle("X");
        updatedEntity.setDescription("Y");
        updatedEntity.setCourse(courseEntity);
        updatedEntity.setCreatedBy(userEntity);

        when(quizRepo.findById(99L)).thenReturn(Optional.of(quizEntity));
        when(courseRepo.findById(5L)).thenReturn(Optional.of(courseEntity));
        when(userRepo.findById(7L)).thenReturn(Optional.of(userEntity));
        when(quizRepo.save(any())).thenReturn(updatedEntity);

        Quiz out = service.updateQuiz(99L, updated);

        assertEquals("X", out.getTitle());
        verify(quizRepo).save(any());
    }

    @Test
    void updateQuizNotFound() {
        when(quizRepo.findById(1L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateQuiz(1L, quiz));
        assertTrue(ex.getMessage().contains("Could not update quiz"));
    }

    @Test
    void deleteQuizSuccess() {
        when(quizRepo.existsById(99L)).thenReturn(true);
        assertDoesNotThrow(() -> service.deleteQuiz(99L));
        verify(quizRepo).deleteById(99L);
    }

    @Test
    void deleteQuizNotFound() {
        when(quizRepo.existsById(1L)).thenReturn(false);
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.deleteQuiz(1L));
        assertEquals("Could not delete quiz.", ex.getMessage());
    }
}
