package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.Course;
import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAllCourses();
    Optional<Course> getCourseById(Long id);
    List<Course> getCoursesByInstructor(Long instructorId);
    Course createCourse(Course course);
    Optional<Course> updateCourse(Long id, Course course);
    boolean deleteCourse(Long id);
}
