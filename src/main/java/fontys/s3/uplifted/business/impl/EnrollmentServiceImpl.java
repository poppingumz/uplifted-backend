package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.EnrollmentService;
import fontys.s3.uplifted.domain.Enrollment;
import fontys.s3.uplifted.persistence.EnrollmentRepository;
import fontys.s3.uplifted.persistence.entity.EnrollmentEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.getAllEnrollments()
                .stream()
                .map(EnrollmentMapper::convert)
                .collect(Collectors.toList());
    }

    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.getEnrollmentById(id).map(EnrollmentMapper::convert);
    }

    public Enrollment createEnrollment(Enrollment enrollment) {
        EnrollmentEntity entity = EnrollmentMapper.convertToEntity(enrollment);
        EnrollmentEntity savedEntity = enrollmentRepository.createEnrollment(entity);
        return EnrollmentMapper.convert(savedEntity);
    }

    public Optional<Enrollment> updateEnrollment(Long id, Enrollment enrollment) {
        EnrollmentEntity entity = EnrollmentMapper.convertToEntity(enrollment);
        return enrollmentRepository.updateEnrollment(id, entity).map(EnrollmentMapper::convert);
    }

    public boolean deleteEnrollment(Long id) {
        return enrollmentRepository.deleteEnrollment(id);
    }

    public List<Enrollment> getEnrollmentsByCourseId(Long courseId) {
        return enrollmentRepository.getEnrollmentsByCourseId(courseId)
                .stream()
                .map(EnrollmentMapper::convert)
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsByUserId(Long userId) {
        return enrollmentRepository.getEnrollmentsByUserId(userId)
                .stream()
                .map(EnrollmentMapper::convert)
                .collect(Collectors.toList());
    }

    public boolean isUserEnrolledInCourse(Long userId, Long courseId) {
        return enrollmentRepository.isUserEnrolledInCourse(userId, courseId);
    }
}
