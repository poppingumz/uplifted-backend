package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.domain.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private Course sampleCourse;
    private MultipartFile mockImage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleCourse = Course.builder()
                .id(1L)
                .title("Java Basics")
                .description("Intro to Java")
                .instructorId(10L)
                .enrolledStudentIds(Set.of(101L, 102L))
                .enrollmentLimit(50)
                .published(true)
                .category("Programming")
                .build();

        mockImage = new MockMultipartFile("image", "test.jpg", "image/jpeg", "image content".getBytes());
    }

    @Test
    void shouldReturnAllCourses() {
        when(courseService.getAllCourses()).thenReturn(List.of(sampleCourse));

        ResponseEntity<List<Course>> response = courseController.getAllCourses();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(courseService, times(1)).getAllCourses();
    }

    @Test
    void shouldReturnCourseByIdIfExists() {
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(sampleCourse));

        ResponseEntity<Course> response = courseController.getCourseById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleCourse, response.getBody());
        verify(courseService, times(1)).getCourseById(1L);
    }

    @Test
    void shouldReturnNotFoundIfCourseDoesNotExist() {
        when(courseService.getCourseById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Course> response = courseController.getCourseById(99L);

        assertEquals(404, response.getStatusCodeValue());
        verify(courseService, times(1)).getCourseById(99L);
    }

    @Test
    void shouldCreateNewCourseWithImage() throws Exception {
        when(courseService.createCourse(sampleCourse)).thenReturn(sampleCourse);

        ResponseEntity<Course> response = courseController.createCourse(sampleCourse, mockImage);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleCourse, response.getBody());
        verify(courseService, times(1)).createCourse(sampleCourse);
    }

    @Test
    void shouldUpdateExistingCourseWithImage() throws Exception {
        when(courseService.updateCourse(eq(1L), any(Course.class))).thenReturn(Optional.of(sampleCourse));

        ResponseEntity<Course> response = courseController.updateCourse(1L, sampleCourse, mockImage);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleCourse, response.getBody());
        verify(courseService, times(1)).updateCourse(eq(1L), any(Course.class));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingCourse() throws Exception {
        when(courseService.updateCourse(eq(99L), any(Course.class))).thenReturn(Optional.empty());

        ResponseEntity<Course> response = courseController.updateCourse(99L, sampleCourse, mockImage);

        assertEquals(404, response.getStatusCodeValue());
        verify(courseService, times(1)).updateCourse(eq(99L), any(Course.class));
    }

    @Test
    void shouldDeleteCourseIfExists() {
        when(courseService.deleteCourse(1L)).thenReturn(true);

        ResponseEntity<Void> response = courseController.deleteCourse(1L);

        assertEquals(204, response.getStatusCodeValue());
        verify(courseService, times(1)).deleteCourse(1L);
    }

    @Test
    void shouldReturnNotFoundIfCourseToDeleteDoesNotExist() {
        when(courseService.deleteCourse(99L)).thenReturn(false);

        ResponseEntity<Void> response = courseController.deleteCourse(99L);

        assertEquals(404, response.getStatusCodeValue());
        verify(courseService, times(1)).deleteCourse(99L);
    }
}
