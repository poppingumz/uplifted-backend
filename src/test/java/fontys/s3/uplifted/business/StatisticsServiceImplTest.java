package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.StatisticsServiceImpl;
import fontys.s3.uplifted.domain.dto.CourseCategoryStatDTO;
import fontys.s3.uplifted.domain.dto.CourseEnrollmentStatDTO;
import fontys.s3.uplifted.domain.enums.InterestCategory;
import fontys.s3.uplifted.persistence.StatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatisticsServiceImplTest {

    private StatisticsRepository repo;
    private StatisticsServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(StatisticsRepository.class);
        service = new StatisticsServiceImpl(repo);
    }

    @Test
    void getCoursesPerCategory_returnsList() {
        List<CourseCategoryStatDTO> mockStats = List.of(
                new CourseCategoryStatDTO(InterestCategory.TECHNOLOGY, 3),
                new CourseCategoryStatDTO(InterestCategory.DESIGN, 5)
        );

        when(repo.getCoursesPerCategory()).thenReturn(mockStats);

        List<CourseCategoryStatDTO> result = service.getCoursesPerCategory();

        assertEquals(2, result.size());
        assertEquals(InterestCategory.TECHNOLOGY, result.get(0).category());
        assertEquals(3, result.get(0).courseCount());
    }

    @Test
    void getStudentsPerCourse_returnsList() {
        List<CourseEnrollmentStatDTO> mockStats = List.of(
                new CourseEnrollmentStatDTO("Java Basics", 10),
                new CourseEnrollmentStatDTO("React UI", 7)
        );

        when(repo.getStudentsPerCourse()).thenReturn(mockStats);

        List<CourseEnrollmentStatDTO> result = service.getStudentsPerCourse();

        assertEquals(2, result.size());
        assertEquals("Java Basics", result.get(0).courseTitle());
        assertEquals(10, result.get(0).studentCount());
    }
}
