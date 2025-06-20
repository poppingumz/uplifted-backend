package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.domain.dto.CourseCategoryStatDTO;
import fontys.s3.uplifted.domain.dto.CourseEnrollmentStatDTO;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import java.util.List;

@org.springframework.stereotype.Repository
public interface StatisticsRepository extends Repository<CourseEntity, Long> {

    @Query("SELECT new fontys.s3.uplifted.domain.dto.CourseCategoryStatDTO(c.category, COUNT(c)) " +
            "FROM CourseEntity c GROUP BY c.category")
    List<CourseCategoryStatDTO> getCoursesPerCategory();

    @Query("SELECT new fontys.s3.uplifted.domain.dto.CourseEnrollmentStatDTO(e.course.title, COUNT(e)) " +
            "FROM EnrollmentEntity e GROUP BY e.course.title")
    List<CourseEnrollmentStatDTO> getStudentsPerCourse();
}
