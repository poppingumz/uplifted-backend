package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.domain.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseControllerTest {
    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private Course teacherCourse, otherTeacherCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        teacherCourse = new Course(1L, 10L, "Java Basics", "Intro to Java", Set.of(101L, 102L));
        otherTeacherCourse = new Course(2L, 11L, "Python Basics", "Intro to Python", Set.of(103L, 104L));
    }

    @Test
    void shouldAllowTeacherToUpdateOwnCourse() {
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(teacherCourse));
        when(courseService.updateCourse(eq(1L), any(Course.class))).thenReturn(Optional.of(teacherCourse));

        ResponseEntity<?> response = courseController.updateCourse(1L, teacherCourse, "TEACHER", 10L);

        assertEquals(200, response.getStatusCodeValue());
        verify(courseService, times(1)).updateCourse(eq(1L), any(Course.class));
    }

    @Test
    void shouldNotAllowTeacherToUpdateOtherTeacherCourse() {
        when(courseService.getCourseById(2L)).thenReturn(Optional.of(otherTeacherCourse));

        ResponseEntity<?> response = courseController.updateCourse(2L, teacherCourse, "TEACHER", 10L);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("You can only modify your own courses.", response.getBody());
        verify(courseService, never()).updateCourse(eq(2L), any(Course.class));
    }

    @Test
    void shouldAllowTeacherToDeleteOwnCourse() {
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(teacherCourse));
        when(courseService.deleteCourse(1L)).thenReturn(true);

        ResponseEntity<?> response = courseController.deleteCourse(1L, "TEACHER", 10L);

        assertEquals(204, response.getStatusCodeValue());
        verify(courseService, times(1)).deleteCourse(1L);
    }

    @Test
    void shouldNotAllowTeacherToDeleteOtherTeacherCourse() {
        when(courseService.getCourseById(2L)).thenReturn(Optional.of(otherTeacherCourse));

        ResponseEntity<?> response = courseController.deleteCourse(2L, "TEACHER", 10L);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("You can only delete your own courses.", response.getBody());
        verify(courseService, never()).deleteCourse(2L);
    }
}
