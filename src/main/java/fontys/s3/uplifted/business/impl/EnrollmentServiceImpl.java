package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.EnrollmentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final Map<Long, Set<Long>> enrollments = new HashMap<>();

    public void enrollStudent(Long courseId, Long studentId) {
        try {
            enrollments.computeIfAbsent(courseId, k -> new HashSet<>()).add(studentId);
            log.info("Enrolled student {} to course {}", studentId, courseId);
        } catch (Exception e) {
            log.error("Failed to enroll student {} to course {}", studentId, courseId, e);
            throw new RuntimeException("Unable to enroll student.");
        }
    }

    public void unenrollStudent(Long courseId, Long studentId) {
        try {
            Set<Long> students = enrollments.get(courseId);
            if (students != null && students.remove(studentId)) {
                log.info("Unenrolled student {} from course {}", studentId, courseId);
            } else {
                log.warn("Student {} was not enrolled in course {}", studentId, courseId);
            }
        } catch (Exception e) {
            log.error("Failed to unenroll student {} from course {}", studentId, courseId, e);
            throw new RuntimeException("Unable to unenroll student.");
        }
    }

    public Set<Long> getEnrolledStudents(Long courseId) {
        try {
            return new HashSet<>(enrollments.getOrDefault(courseId, Collections.emptySet()));
        } catch (Exception e) {
            log.error("Failed to retrieve enrolled students for course {}", courseId, e);
            throw new RuntimeException("Unable to get enrolled students.");
        }
    }
}
