package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.CourseServiceImpl;
import fontys.s3.uplifted.business.impl.exception.CourseNotFoundException;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.CoursePart;
import fontys.s3.uplifted.domain.CoursePartContent;
import fontys.s3.uplifted.domain.enums.ContentType;
import fontys.s3.uplifted.domain.enums.InterestCategory;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.EnrollmentRepository;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceImplTest {

    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private EnrollmentRepository enrollmentRepository;
    private EnrollmentService enrollmentService;
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        userRepository = mock(UserRepository.class);
        fileRepository = mock(FileRepository.class);
        enrollmentRepository = mock(EnrollmentRepository.class);
        enrollmentService = mock(EnrollmentService.class);

        courseService = new CourseServiceImpl(
                courseRepository,
                userRepository,
                fileRepository,
                enrollmentRepository,
                enrollmentService
        );
    }

    @Test
    void getAllCourses_success() {
        CourseEntity course = CourseEntity.builder().id(1L).title("Test").published(true)
                .instructor(UserEntity.builder().id(100L).username("inst").build()).category(InterestCategory.PROGRAMMING).build();
        when(courseRepository.findAll()).thenReturn(List.of(course));
        List<Course> courses = courseService.getAllCourses();
        assertEquals(1, courses.size());
    }

    @Test
    void getCoursesByInstructor_success() {
        CourseEntity course = CourseEntity.builder().id(1L).title("Physics").category(InterestCategory.DATA_SCIENCE)
                .instructor(UserEntity.builder().id(100L).build()).build();
        when(courseRepository.findByInstructorId(100L)).thenReturn(List.of(course));
        List<Course> courses = courseService.getCoursesByInstructor(100L);
        assertEquals("Physics", courses.get(0).getTitle());
    }

    @Test
    void getCourseById_success() {
        CourseEntity course = CourseEntity.builder().id(1L).title("Math").published(true)
                .instructor(UserEntity.builder().id(100L).username("inst").build()).category(InterestCategory.TECHNOLOGY).build();
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
        List<EnrollmentEntity> enrollments = List.of(new EnrollmentEntity(1L, 10L));
        when(enrollmentRepository.findByUserId(10L)).thenReturn(enrollments);

        CourseEntity course = CourseEntity.builder()
                .id(1L)
                .title("Test Course")
                .instructor(UserEntity.builder().id(100L).build())
                .category(InterestCategory.PROGRAMMING)
                .build();
        when(courseRepository.findAllById(List.of(1L))).thenReturn(List.of(course));

        List<Course> result = courseService.getCoursesByEnrolledUser(10L);
        assertEquals(1, result.size());
        assertEquals("Test Course", result.get(0).getTitle());
    }

    @Test
    void enrollInCourse_success() {
        UserEntity user = UserEntity.builder().id(10L).username("user1").build();
        CourseEntity course = CourseEntity.builder().id(1L).title("Test")
                .enrollmentLimit(5).category(InterestCategory.PROGRAMMING).build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findByEmail("user1")).thenReturn(Optional.of(user));
        when(enrollmentService.getEnrolledStudents(1L)).thenReturn(Set.of());

        courseService.enrollInCourse(1L, "user1");

        verify(enrollmentService).enrollStudent(1L, 10L);
    }

    @Test
    void enrollInCourse_alreadyEnrolled_throwsException() {
        UserEntity user = UserEntity.builder().id(10L).username("user1").build();
        CourseEntity course = CourseEntity.builder().id(1L).title("Test")
                .enrollmentLimit(5).category(InterestCategory.PROGRAMMING).build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findByEmail("user1")).thenReturn(Optional.of(user));
        when(enrollmentService.getEnrolledStudents(1L)).thenReturn(Set.of(10L));

        doThrow(new IllegalStateException("Already enrolled"))
                .when(enrollmentService).enrollStudent(1L, 10L);

        assertThrows(IllegalStateException.class, () -> courseService.enrollInCourse(1L, "user1"));
    }

    @Test
    void enrollInCourse_limitReached_throwsException() {
        UserEntity user = UserEntity.builder().id(10L).username("user1").build();
        CourseEntity course = CourseEntity.builder().id(1L).title("Test")
                .enrollmentLimit(5).category(InterestCategory.PROGRAMMING).build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findByEmail("user1")).thenReturn(Optional.of(user));
        when(enrollmentService.getEnrolledStudents(1L)).thenReturn(Set.of(1L, 2L, 3L, 4L, 5L));

        assertThrows(IllegalStateException.class, () -> courseService.enrollInCourse(1L, "user1"));
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

        List<CoursePartEntity> mutableParts = new ArrayList<>();
        mutableParts.add(part);

        CourseEntity existing = CourseEntity.builder()
                .id(1L)
                .title("Old Title")
                .instructor(instructor)
                .category(InterestCategory.PROGRAMMING)
                .parts(mutableParts)
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

    @Test
    void unenrollFromCourse_success() {
        CourseEntity course = CourseEntity.builder().id(1L).title("Test").build();
        UserEntity user = UserEntity.builder().id(10L).email("user@example.com").build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        courseService.unenrollFromCourse(1L, "user@example.com");
        verify(enrollmentService).unenrollStudent(1L, 10L);
    }

    @Test
    void buildPartEntity_and_buildContentEntity_viaReflection() throws Exception {
        CoursePartContent dtoContent = new CoursePartContent("TitleX", ContentType.FILE, 123L);
        CoursePart dtoPart = new CoursePart("PartTitle", 2, 5, List.of(dtoContent));
        CourseEntity courseEntity = CourseEntity.builder().id(99L).build();

        Method buildPart = CourseServiceImpl.class
                .getDeclaredMethod("buildPartEntity", CoursePart.class, CourseEntity.class);
        buildPart.setAccessible(true);

        CoursePartEntity partEntity = (CoursePartEntity) buildPart.invoke(courseService, dtoPart, courseEntity);

        assertEquals("PartTitle", partEntity.getTitle());
        assertEquals(2, partEntity.getWeekNumber());
        assertEquals(5, partEntity.getSequence());
        assertSame(courseEntity, partEntity.getCourse());
        assertNotNull(partEntity.getContents());
        assertEquals(1, partEntity.getContents().size());

        CoursePartContentEntity contentEntity = partEntity.getContents().get(0);
        assertEquals(ContentType.FILE, contentEntity.getContentType());
        assertEquals(123L, contentEntity.getContentId());
        assertEquals("TitleX", contentEntity.getTitle());
        assertSame(partEntity, contentEntity.getPart());
    }


}
