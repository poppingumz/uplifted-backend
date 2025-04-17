package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.CourseServiceImpl;
import fontys.s3.uplifted.business.impl.mapper.CourseMapper;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    private CourseRepository courseRepository;
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        courseService = new CourseServiceImpl(courseRepository);
    }

    @Test
    void getCourseById_success() {
        CourseEntity courseEntity = CourseEntity.builder().id(1L).title("Math").build();
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));

        Optional<Course> result = courseService.getCourseById(1L);
        assertTrue(result.isPresent());
        assertEquals("Math", result.get().getTitle());
    }

    @Test
    void deleteCourse_notFound() {
        when(courseRepository.existsById(5L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> courseService.deleteCourse(5L));
    }
}
