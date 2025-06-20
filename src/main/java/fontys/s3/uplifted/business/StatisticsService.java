package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.dto.CourseCategoryStatDTO;
import fontys.s3.uplifted.domain.dto.CourseEnrollmentStatDTO;

import java.util.List;

public interface StatisticsService {
    List<CourseCategoryStatDTO> getCoursesPerCategory();
    List<CourseEnrollmentStatDTO> getStudentsPerCourse();
}
