package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.dto.CourseResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> getAllCourses();
    Optional<Course> getCourseById(Long id);
    List<Course> getCoursesByInstructor(Long instructorId);
    Course createCourse(Course course, List<MultipartFile> uploadedFiles);
    Optional<Course> updateCourse(Long id, Course course, List<MultipartFile> uploadedFiles);
    boolean deleteCourse(Long id);
    List<Course> getCoursesByEnrolledUser(Long userId);
    void enrollInCourse(Long courseId, String username);
    void unenrollFromCourse(Long courseId, String username);
    Page<CourseResponseDTO> getFilteredCourses(String title, String category, String sort, int page);
}
