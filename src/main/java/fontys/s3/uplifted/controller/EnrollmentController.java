package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.EnrollmentService;
import fontys.s3.uplifted.domain.Enrollment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Enrollment createEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentService.createEnrollment(enrollment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable Long id, @RequestBody Enrollment enrollment) {
        return enrollmentService.updateEnrollment(id, enrollment)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        return enrollmentService.deleteEnrollment(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/course/{courseId}")
    public List<Enrollment> getEnrollmentsByCourseId(@PathVariable Long courseId) {
        return enrollmentService.getEnrollmentsByCourseId(courseId);
    }

    @GetMapping("/user/{userId}")
    public List<Enrollment> getEnrollmentsByUserId(@PathVariable Long userId) {
        return enrollmentService.getEnrollmentsByUserId(userId);
    }

    @GetMapping("/check/{userId}/{courseId}")
    public boolean isUserEnrolled(@PathVariable Long userId, @PathVariable Long courseId) {
        return enrollmentService.isUserEnrolledInCourse(userId, courseId);
    }

    @PostMapping("/{courseId}/users/{userId}")
    public ResponseEntity<?> enrollUser(@PathVariable Long courseId, @PathVariable Long userId) {
        try {
            enrollmentService.enrollUser(courseId, userId);
            return ResponseEntity.ok("User enrolled successfully!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
