package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.StatisticsService;
import fontys.s3.uplifted.domain.dto.CourseCategoryStatDTO;
import fontys.s3.uplifted.domain.dto.CourseEnrollmentStatDTO;
import fontys.s3.uplifted.persistence.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository repo;

    @Override
    public List<CourseCategoryStatDTO> getCoursesPerCategory() {
        return repo.getCoursesPerCategory();
    }

    @Override
    public List<CourseEnrollmentStatDTO> getStudentsPerCourse() {
        return repo.getStudentsPerCourse();
    }
}
