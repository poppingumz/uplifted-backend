package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.Enrollment;
import java.util.List;
import java.util.Optional;

public interface EnrollmentService {
    List<Enrollment> getAllEnrollments();
    Optional<Enrollment> getEnrollmentById(Long id);
    Enrollment createEnrollment(Enrollment enrollment);
    Optional<Enrollment> updateEnrollment(Long id, Enrollment enrollment);
    boolean deleteEnrollment(Long id);
    List<Enrollment> getEnrollmentsByCourseId(Long courseId);
    List<Enrollment> getEnrollmentsByUserId(Long userId);
    boolean isUserEnrolledInCourse(Long userId, Long courseId);
}
