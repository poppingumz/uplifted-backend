package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseRepository  {
    List<CourseEntity> getAllCourses();
    Optional<CourseEntity> getCourseById(Long id);
    CourseEntity createCourse(CourseEntity course);
    Optional<CourseEntity> updateCourse(Long id, CourseEntity updatedCourse);
    boolean deleteCourse(Long id);
}
