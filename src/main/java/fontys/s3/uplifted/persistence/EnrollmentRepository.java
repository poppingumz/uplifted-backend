package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.EnrollmentEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EnrollmentRepository {
    List<EnrollmentEntity> getAllEnrollments();
    Optional<EnrollmentEntity> getEnrollmentById(Long id);
    EnrollmentEntity createEnrollment(EnrollmentEntity enrollment);
    Optional<EnrollmentEntity> updateEnrollment(Long id, EnrollmentEntity enrollment);
    boolean deleteEnrollment(Long id);
    List<EnrollmentEntity> getEnrollmentsByCourseId(Long courseId);
    List<EnrollmentEntity> getEnrollmentsByUserId(Long userId);
    boolean isUserEnrolledInCourse(Long userId, Long courseId);
}
