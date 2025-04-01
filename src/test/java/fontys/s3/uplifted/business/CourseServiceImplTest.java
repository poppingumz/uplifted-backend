package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.CourseServiceImpl;
import fontys.s3.uplifted.business.impl.mapper.CourseMapper;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private CourseEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        course = Course.builder()
                .id(1L)
                .title("Java")
                .description("Learn Java")
                .instructorName("Alice")
                .enrolledStudentIds(Set.of(1L))
                .build();

        entity = CourseMapper.toEntity(course);
    }

    @Test
    void testGetAllCourses() {
        when(courseRepository.getAllCourses()).thenReturn(List.of(entity));
        List<Course> result = courseService.getAllCourses();
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getTitle());
    }

    @Test
    void testGetCourseById() {
        when(courseRepository.getCourseById(1L)).thenReturn(Optional.of(entity));
        Optional<Course> result = courseService.getCourseById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void testCreateCourse() {
        when(courseRepository.createCourse(any())).thenReturn(entity);
        Course created = courseService.createCourse(course);
        assertEquals("Java", created.getTitle());
    }

    @Test
    void testUpdateCourse_Found() {
        when(courseRepository.getCourseById(1L)).thenReturn(Optional.of(entity));
        when(courseRepository.updateCourse(eq(1L), any())).thenReturn(Optional.of(entity));
        Optional<Course> result = courseService.updateCourse(1L, course);
        assertTrue(result.isPresent());
    }

    @Test
    void testDeleteCourse_Success() {
        when(courseRepository.deleteCourse(1L)).thenReturn(true);
        assertTrue(courseService.deleteCourse(1L));
    }
}
