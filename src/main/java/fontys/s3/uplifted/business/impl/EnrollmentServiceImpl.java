package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.EnrollmentService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final Map<Long, Set<Long>> enrollments = new HashMap<>();

    @Override
    public void enrollStudent(Long courseId, Long studentId) {
        enrollments.computeIfAbsent(courseId, k -> new HashSet<>()).add(studentId);
    }

    @Override
    public void unenrollStudent(Long courseId, Long studentId) {
        enrollments.getOrDefault(courseId, new HashSet<>()).remove(studentId);
    }

    @Override
    public Set<Long> getEnrolledStudents(Long courseId) {
        return enrollments.getOrDefault(courseId, new HashSet<>());
    }
}
