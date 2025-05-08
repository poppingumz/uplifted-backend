package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.CourseServiceImpl;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        userRepository = mock(UserRepository.class);
        courseService = new CourseServiceImpl(courseRepository, userRepository);
    }

    @Test
    void getCourseById_success() {
        UserEntity instructor = UserEntity.builder()
                .id(100L)
                .username("instructor1")
                .email("instructor1@example.com")
                .password("securePass")
                .role(Role.TEACHER)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        CourseEntity courseEntity = CourseEntity.builder()
                .id(1L)
                .title("Math")
                .description("Algebra basics")
                .published(true)
                .enrollmentLimit(50)
                .instructor(instructor)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));

        Optional<Course> result = courseService.getCourseById(1L);

        assertTrue(result.isPresent());
        assertEquals("Math", result.get().getTitle());
        assertEquals(100L, result.get().getInstructorId());
    }

    @Test
    void deleteCourse_notFound() {
        when(courseRepository.existsById(5L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> courseService.deleteCourse(5L));
    }
}
