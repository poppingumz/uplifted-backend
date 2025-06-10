package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.CourseServiceImpl;
import fontys.s3.uplifted.business.impl.exception.CourseNotFoundException;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.dto.NotificationMessage;
import fontys.s3.uplifted.domain.enums.ContentType;
import fontys.s3.uplifted.domain.enums.InterestCategory;
import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private SimpMessagingTemplate messagingTemplate;
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        userRepository = mock(UserRepository.class);
        fileRepository = mock(FileRepository.class);
        messagingTemplate = mock(SimpMessagingTemplate.class);
        courseService = new CourseServiceImpl(courseRepository, userRepository, fileRepository, messagingTemplate);
    }

    @Test
    void getAllCourses_success() {
        CourseEntity course = CourseEntity.builder().id(1L).title("Test").published(true)
                .instructor(UserEntity.builder().id(100L).username("inst").build()).build();
        when(courseRepository.findAll()).thenReturn(List.of(course));
        List<Course> courses = courseService.getAllCourses();
        assertEquals(1, courses.size());
    }

    @Test
    void getCoursesByInstructor_success() {
        CourseEntity course = CourseEntity.builder().id(1L).title("Physics")
                .instructor(UserEntity.builder().id(100L).build()).build();
        when(courseRepository.findByInstructorId(100L)).thenReturn(List.of(course));
        List<Course> courses = courseService.getCoursesByInstructor(100L);
        assertEquals("Physics", courses.get(0).getTitle());
    }

    @Test
    void getCourseById_success() {
        CourseEntity course = CourseEntity.builder().id(1L).title("Math").published(true)
                .instructor(UserEntity.builder().id(100L).username("inst").build()).build();
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Optional<Course> result = courseService.getCourseById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void deleteCourse_notFound() {
        when(courseRepository.existsById(10L)).thenReturn(false);
        assertThrows(CourseNotFoundException.class, () -> courseService.deleteCourse(10L));
    }

    @Test
    void deleteCourse_success() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        courseService.deleteCourse(1L);
        verify(courseRepository).deleteById(1L);
    }

    @Test
    void getCoursesByEnrolledUser_success() {
        UserEntity student = UserEntity.builder().id(10L).username("student").build();
        CourseEntity course = CourseEntity.builder()
                .id(1L)
                .title("Test Course")
                .enrolledStudents(Set.of(student))
                .instructor(UserEntity.builder().id(100L).build())
                .build();
        when(courseRepository.findAll()).thenReturn(List.of(course));
        List<Course> result = courseService.getCoursesByEnrolledUser(10L);
        assertEquals(1, result.size());
    }

    @Test
    void enrollInCourse_success() {
        UserEntity user = UserEntity.builder().id(10L).username("user1").build();
        CourseEntity course = CourseEntity.builder().id(1L).title("Test").enrolledStudents(new HashSet<>())
                .enrollmentLimit(5).build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        courseService.enrollInCourse(1L, "user1");
        assertTrue(course.getEnrolledStudents().contains(user));
        verify(courseRepository).save(course);
    }

    @Test
    void enrollInCourse_alreadyEnrolled_throwsException() {
        UserEntity user = UserEntity.builder().id(10L).username("user1").build();
        CourseEntity course = CourseEntity.builder().id(1L).title("Test").enrolledStudents(new HashSet<>(List.of(user)))
                .enrollmentLimit(5).build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> courseService.enrollInCourse(1L, "user1"));
    }

    @Test
    void enrollInCourse_limitReached_throwsException() {
        UserEntity user = UserEntity.builder().id(10L).username("user1").build();
        Set<UserEntity> enrolled = new HashSet<>();
        for (int i = 0; i < 5; i++) enrolled.add(UserEntity.builder().id((long) i).build());

        CourseEntity course = CourseEntity.builder().id(1L).title("Test").enrolledStudents(enrolled)
                .enrollmentLimit(5).build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class, () -> courseService.enrollInCourse(1L, "user1"));
    }

    @Test
    void createCourse_withFiles_success() throws Exception {
        UserEntity instructor = UserEntity.builder().id(100L).username("instructor").build();
        CoursePartContentEntity content = CoursePartContentEntity.builder().contentType(ContentType.FILE).title("doc").build();
        CoursePartEntity part = CoursePartEntity.builder().contents(List.of(content)).build();
        CourseEntity entity = CourseEntity.builder().id(1L).title("Course").instructor(instructor).parts(List.of(part)).category(InterestCategory.TECHNOLOGY).build();

        Course input = Course.builder().instructorId(100L).title("Course").category(InterestCategory.TECHNOLOGY).build();
        MultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "abc".getBytes());

        when(userRepository.findById(100L)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(CourseEntity.class))).thenReturn(entity);
        when(fileRepository.save(any())).thenReturn(FileEntity.builder().id(999L).build());

        Course created = courseService.createCourse(input, List.of(file));
        assertEquals("Course", created.getTitle());

        verify(messagingTemplate, times(2)).convertAndSend(any(String.class), any(NotificationMessage.class));
    }

    @Test
    void updateCourse_withFiles_success() throws Exception {
        UserEntity instructor = UserEntity.builder().id(100L).username("instructor").build();

        CoursePartContentEntity content = CoursePartContentEntity.builder()
                .title("PDF")
                .contentType(ContentType.FILE)
                .build();

        CoursePartEntity part = CoursePartEntity.builder()
                .title("Week 1")
                .contents(List.of(content))
                .build();

        CourseEntity existing = CourseEntity.builder()
                .id(1L)
                .title("Old Title")
                .instructor(instructor)
                .category(InterestCategory.PROGRAMMING)
                .parts(List.of(part)) // ✅ this line fixes your error
                .build();

        Course updatedCourse = Course.builder()
                .title("Updated Title")
                .instructorId(100L)
                .category(InterestCategory.PROGRAMMING)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.findById(100L)).thenReturn(Optional.of(instructor));
        when(fileRepository.save(any())).thenReturn(FileEntity.builder().id(500L).build());
        when(courseRepository.save(any())).thenReturn(existing);

        MultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "abc".getBytes());

        Optional<Course> result = courseService.updateCourse(1L, updatedCourse, List.of(file));

        assertTrue(result.isPresent());
        assertEquals("Updated Title", result.get().getTitle());
    }

}
