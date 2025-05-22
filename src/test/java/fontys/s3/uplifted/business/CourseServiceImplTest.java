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
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
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
    void getAllCourses_success() {
        when(courseRepository.findAll()).thenReturn(List.of(CourseEntity.builder()
                .id(1L).title("Test").description("Test Desc").published(true).enrollmentLimit(10)
                .instructor(UserEntity.builder().id(100L).username("inst").build()).build()));

        List<Course> courses = courseService.getAllCourses();
        assertEquals(1, courses.size());
        assertEquals("Test", courses.get(0).getTitle());
    }

    @Test
    void getCoursesByInstructor_success() {
        when(courseRepository.findByInstructorId(100L)).thenReturn(List.of(CourseEntity.builder()
                .id(1L).title("Physics").instructor(UserEntity.builder().id(100L).build()).build()));

        List<Course> courses = courseService.getCoursesByInstructor(100L);
        assertEquals(1, courses.size());
        assertEquals("Physics", courses.get(0).getTitle());
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

    @Test
    void createCourse_success() {
        UserEntity instructor = UserEntity.builder()
                .id(100L)
                .username("instructor1")
                .email("instructor1@example.com")
                .password("securePass")
                .role(Role.TEACHER)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        CourseEntity savedEntity = CourseEntity.builder()
                .id(1L)
                .title("New Course")
                .description("Some description")
                .published(true)
                .enrollmentLimit(20)
                .instructor(instructor)
                .build();

        Course input = Course.builder()
                .title("New Course")
                .description("Some description")
                .published(true)
                .enrollmentLimit(20)
                .build();

        try (MockedStatic<SecurityContextHolder> mockSecurity = mockStatic(SecurityContextHolder.class)) {
            Authentication auth = mock(Authentication.class);
            SecurityContext context = mock(SecurityContext.class);

            when(context.getAuthentication()).thenReturn(auth);
            when(auth.getName()).thenReturn("instructor1");
            mockSecurity.when(SecurityContextHolder::getContext).thenReturn(context);

            when(userRepository.findByUsername("instructor1")).thenReturn(Optional.of(instructor));
            when(courseRepository.save(any(CourseEntity.class))).thenReturn(savedEntity);

            Course created = courseService.createCourse(input);

            assertNotNull(created);
            assertEquals("New Course", created.getTitle());
            assertEquals(100L, created.getInstructorId());
        }
    }

    @Test
    void updateCourse_success() {
        UserEntity instructor = UserEntity.builder()
                .id(100L)
                .username("instructor1")
                .email("instructor1@example.com")
                .password("securePass")
                .role(Role.TEACHER)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build();

        CourseEntity existing = CourseEntity.builder()
                .id(1L)
                .title("Old Title")
                .description("Old")
                .enrollmentLimit(10)
                .published(false)
                .instructor(instructor)
                .build();

        CourseEntity updatedEntity = CourseEntity.builder()
                .id(1L)
                .title("Updated Title")
                .description("New Description")
                .published(true)
                .enrollmentLimit(40)
                .instructor(instructor)
                .build();

        Course updated = Course.builder()
                .title("Updated Title")
                .description("New Description")
                .published(true)
                .enrollmentLimit(40)
                .instructorId(100L)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(100L)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(CourseEntity.class))).thenReturn(updatedEntity);

        Optional<Course> result = courseService.updateCourse(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("Updated Title", result.get().getTitle());
    }
}
