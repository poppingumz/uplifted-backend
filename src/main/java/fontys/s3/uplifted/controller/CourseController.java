package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.domain.Course;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
        try {
            return ResponseEntity.ok(courseService.getAllCourses());
        } catch (Exception e) {
            log.error("Failed to fetch courses", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        try {
            return courseService.getCourseById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error retrieving course with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/instructor/{userId}")
    public ResponseEntity<List<Course>> getCoursesByInstructor(@PathVariable Long userId) {
        try {
            List<Course> courses = courseService.getCoursesByInstructor(userId);
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            log.error("Error fetching courses for instructor ID: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Course> createCourse(
            @RequestPart("course") Course course,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            if (image != null && !image.isEmpty()) {
                course.setImageData(image.getBytes());
            }
            Course created = courseService.createCourse(course);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Error creating course: {}", course.getTitle(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @RequestPart("course") Course course,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        try {
            if (image != null && !image.isEmpty()) {
                course.setImageData(image.getBytes());
            }
            return courseService.updateCourse(id, course)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error updating course with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            boolean deleted = courseService.deleteCourse(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting course with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
