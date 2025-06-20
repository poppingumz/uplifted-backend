package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.EnrollmentService;
import fontys.s3.uplifted.business.impl.exception.EnrollmentException;
import fontys.s3.uplifted.persistence.EnrollmentRepository;
import fontys.s3.uplifted.persistence.entity.EnrollmentEntity;
import fontys.s3.uplifted.persistence.entity.EnrollmentId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public void enrollStudent(Long courseId, Long studentId) {
        try {
            EnrollmentEntity entity = new EnrollmentEntity(courseId, studentId);
            enrollmentRepository.save(entity);
            log.info("Persisted enrollment: course={}, user={}", courseId, studentId);
        } catch (Exception e) {
            log.error("Failed to persist enrollment", e);
            throw new EnrollmentException("Unable to enroll student.");
        }
    }

    @Override
    public void unenrollStudent(Long courseId, Long studentId) {
        try {
            enrollmentRepository.deleteById(new EnrollmentId(courseId, studentId));
        } catch (Exception e) {
            throw new EnrollmentException("Unable to unenroll student.");
        }
    }

    @Override
    public Set<Long> getEnrolledStudents(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId)
                .stream()
                .map(EnrollmentEntity::getUserId)
                .collect(Collectors.toSet());
    }

    public Set<Long> getCoursesOfUser(Long userId) {
        return enrollmentRepository.findByUserId(userId)
                .stream()
                .map(EnrollmentEntity::getCourseId)
                .collect(Collectors.toSet());
    }
}
