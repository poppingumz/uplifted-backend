package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.domain.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/instructor/{userId}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(userId));
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Course> createCourse(
            @RequestPart("course") Course course,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        if (image != null && !image.isEmpty()) {
            course.setImageData(image.getBytes());
        }
        Course created = courseService.createCourse(course);
        return ResponseEntity.ok(created);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @RequestPart("course") Course course,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        if (image != null && !image.isEmpty()) {
            course.setImageData(image.getBytes());
        }
        return courseService.updateCourse(id, course)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        boolean deleted = courseService.deleteCourse(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, Authentication authentication) {
        try {
            String username = authentication.getName();
            courseService.enrollInCourse(courseId, username);
            return ResponseEntity.ok("Enrolled successfully");
        } catch (RuntimeException e) {
            log.warn("Enrollment failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Enrollment error", e);
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

}