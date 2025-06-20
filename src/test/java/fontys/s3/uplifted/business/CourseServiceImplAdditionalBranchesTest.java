package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.CourseServiceImpl;
import fontys.s3.uplifted.business.impl.mapper.CourseMapper;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.CoursePart;
import fontys.s3.uplifted.domain.CoursePartContent;
import fontys.s3.uplifted.domain.dto.CourseResponseDTO;
import fontys.s3.uplifted.domain.enums.ContentType;
import fontys.s3.uplifted.domain.enums.InterestCategory;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.EnrollmentRepository;
import fontys.s3.uplifted.persistence.FileRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CourseServiceImplAdditionalBranchesTest {

    private CourseServiceImpl service;
    private CourseRepository courseRepository;
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private EnrollmentRepository enrollmentRepository;
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        courseRepository     = mock(CourseRepository.class);
        userRepository       = mock(UserRepository.class);
        fileRepository       = mock(FileRepository.class);
        enrollmentRepository = mock(EnrollmentRepository.class);
        enrollmentService    = mock(EnrollmentService.class);

        service = new CourseServiceImpl(
                courseRepository,
                userRepository,
                fileRepository,
                enrollmentRepository,
                enrollmentService
        );
    }

    @Test
    void getFilteredCourses_withTitleAndCategory_defaultSort_spec() {
        CourseEntity e = CourseEntity.builder()
                .id(1L)
                .title("MyCourse")
                .category(InterestCategory.PROGRAMMING)
                .instructor(UserEntity.builder().id(5L).build())
                .published(true)
                .build();

        when(courseRepository.findAll(
                any(Specification.class),
                any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(e)));

        var page = service.getFilteredCourses("My", "programming", "unknown", 0);
        assertEquals(1, page.getTotalElements());
        CourseResponseDTO dto = page.getContent().get(0);
        assertEquals("MyCourse", dto.getTitle());
    }

    @Test
    void createCourse_skipsAttachFiles_whenPartsNull() throws Exception {
        UserEntity instr = UserEntity.builder().id(7L).username("t").build();
        when(userRepository.findById(7L)).thenReturn(Optional.of(instr));

        Course dto = Course.builder()
                .title("X")
                .instructorId(7L)
                .parts(null)
                .category(InterestCategory.PROGRAMMING)
                .build();

        CourseEntity stub = CourseEntity.builder()
                .id(1L)
                .title(dto.getTitle())
                .category(dto.getCategory())
                .instructor(instr)
                .build();

        when(courseRepository.save(any()))
                .thenReturn(stub)
                .thenReturn(stub);

        MultipartFile file = new MockMultipartFile(
                "f", "f.txt", "text/plain", "a".getBytes()
        );

        Course out = service.createCourse(dto, List.of(file));
        assertEquals("X", out.getTitle());

        verify(fileRepository, never()).save(any());
    }
    @Test
    void createCourse_skipsAttachFiles_whenUploadedFilesNull() throws Exception {
        UserEntity instr = UserEntity.builder().id(8L).username("u").build();
        when(userRepository.findById(8L)).thenReturn(Optional.of(instr));

        CoursePartContent cDto = new CoursePartContent("T", ContentType.FILE, null);
        CoursePart pDto = new CoursePart("W", 1, 1, List.of(cDto));

        Course dto = Course.builder()
                .title("Y")
                .instructorId(8L)
                .parts(List.of(pDto))
                .category(InterestCategory.PROGRAMMING)
                .build();

        CourseEntity stub = CourseMapper.toEntity(dto, instr);
        when(courseRepository.save(any())).thenReturn(stub, stub);

        Course out = service.createCourse(dto, null);

        assertEquals("Y", out.getTitle());
        verify(fileRepository, never()).save(any());
    }


    @Test
    void updateCourse_skipsPartsWhenDtoPartsEmpty() throws Exception {
        UserEntity instr = UserEntity.builder().id(9L).username("z").build();
        when(userRepository.findById(9L)).thenReturn(Optional.of(instr));

        Course dto = Course.builder()
                .title("Z")
                .instructorId(9L)
                .parts(List.of())
                .category(InterestCategory.TECHNOLOGY)
                .build();

        CourseEntity existing = CourseEntity.builder()
                .id(5L)
                .title("OldZ")
                .instructor(instr)
                .category(InterestCategory.TECHNOLOGY)
                .parts(new java.util.ArrayList<>())
                .build();
        when(courseRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(any())).thenReturn(existing);

        Optional<Course> res = service.updateCourse(5L, dto, null);
        assertTrue(res.isPresent());
        assertTrue(existing.getParts().isEmpty());
    }

}
