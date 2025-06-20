package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.StatisticsService;
import fontys.s3.uplifted.domain.dto.CourseCategoryStatDTO;
import fontys.s3.uplifted.domain.dto.CourseEnrollmentStatDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/courses-per-category")
    public ResponseEntity<List<CourseCategoryStatDTO>> getCoursesPerCategory() {
        return ResponseEntity.ok(statisticsService.getCoursesPerCategory());
    }

    @GetMapping("/students-per-course")
    public ResponseEntity<List<CourseEnrollmentStatDTO>> getStudentsPerCourse() {
        return ResponseEntity.ok(statisticsService.getStudentsPerCourse());
    }
}
