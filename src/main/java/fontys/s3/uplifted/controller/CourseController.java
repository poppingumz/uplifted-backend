package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.domain.Course;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course, @RequestHeader("Role") String role, @RequestHeader("UserId") Long userId) {
        if (!role.equals("TEACHER")) {
            return ResponseEntity.status(403).body("Only teachers can create courses.");
        }
        course.setTeacherId(userId); // Assign the teacher as the course creator
        return ResponseEntity.ok(courseService.createCourse(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course course, @RequestHeader("Role") String role, @RequestHeader("UserId") Long userId) {
        if (!role.equals("TEACHER")) {
            return ResponseEntity.status(403).body("Only teachers can update courses.");
        }

        Course existingCourse = courseService.getCourseById(id).orElse(null);
        if (existingCourse == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existingCourse.getTeacherId().equals(userId)) {
            return ResponseEntity.status(403).body("You can only modify your own courses.");
        }

        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id, @RequestHeader("Role") String role, @RequestHeader("UserId") Long userId) {
        if (!role.equals("TEACHER")) {
            return ResponseEntity.status(403).body("Only teachers can delete courses.");
        }

        Course existingCourse = courseService.getCourseById(id).orElse(null);
        if (existingCourse == null) {
            return ResponseEntity.notFound().build();
        }

        if (!existingCourse.getTeacherId().equals(userId)) {
            return ResponseEntity.status(403).body("You can only delete your own courses.");
        }

        return courseService.deleteCourse(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
