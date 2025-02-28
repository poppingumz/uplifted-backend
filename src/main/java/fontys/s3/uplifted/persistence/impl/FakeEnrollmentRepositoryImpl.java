package fontys.s3.uplifted.persistence.impl;

import fontys.s3.uplifted.persistence.EnrollmentRepository;
import fontys.s3.uplifted.persistence.entity.EnrollmentEntity;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class FakeEnrollmentRepositoryImpl implements EnrollmentRepository {
    private final Map<Long, EnrollmentEntity> enrollments = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public List<EnrollmentEntity> getAllEnrollments() {
        return new ArrayList<>(enrollments.values());
    }

    public Optional<EnrollmentEntity> getEnrollmentById(Long id) {
        return Optional.ofNullable(enrollments.get(id));
    }

    public EnrollmentEntity createEnrollment(EnrollmentEntity enrollment) {
        enrollment.setId(idCounter.getAndIncrement());
        enrollments.put(enrollment.getId(), enrollment);
        return enrollment;
    }

    public Optional<EnrollmentEntity> updateEnrollment(Long id, EnrollmentEntity updatedEnrollment) {
        if (enrollments.containsKey(id)) {
            updatedEnrollment.setId(id);
            enrollments.put(id, updatedEnrollment);
            return Optional.of(updatedEnrollment);
        }
        return Optional.empty();
    }

    public boolean deleteEnrollment(Long id) {
        return enrollments.remove(id) != null;
    }

    public List<EnrollmentEntity> getEnrollmentsByCourseId(Long courseId) {
        return enrollments.values()
                .stream()
                .filter(enrollment -> enrollment.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }

    public List<EnrollmentEntity> getEnrollmentsByUserId(Long userId) {
        return enrollments.values()
                .stream()
                .filter(enrollment -> enrollment.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public boolean isUserEnrolledInCourse(Long userId, Long courseId) {
        return enrollments.values()
                .stream()
                .anyMatch(enrollment -> enrollment.getUserId().equals(userId) && enrollment.getCourseId().equals(courseId));
    }
}
