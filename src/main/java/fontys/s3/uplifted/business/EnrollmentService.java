package fontys.s3.uplifted.business;

import java.util.Set;

public interface EnrollmentService {
    void enrollStudent(Long courseId, Long studentId);
    void unenrollStudent(Long courseId, Long studentId);
    Set<Long> getEnrolledStudents(Long courseId);
}
