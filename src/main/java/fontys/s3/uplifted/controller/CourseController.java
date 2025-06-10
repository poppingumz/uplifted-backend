package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.domain.dto.CourseDTO;
import fontys.s3.uplifted.domain.dto.CourseResponseDTO;
import fontys.s3.uplifted.domain.dto.NotificationMessage;
import fontys.s3.uplifted.business.impl.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return ResponseEntity.ok(
                courseService.getAllCourses().stream().map(CourseMapper::toResponseDTO).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(CourseMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/instructor/{userId}")
    public ResponseEntity<List<CourseResponseDTO>> getCoursesByInstructor(@PathVariable Long userId) {
        return ResponseEntity.ok(
                courseService.getCoursesByInstructor(userId).stream().map(CourseMapper::toResponseDTO).toList()
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCourse(
            @RequestPart("course") CourseDTO courseDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            var course = CourseMapper.toDomain(courseDTO);
            if (image != null && !image.isEmpty()) {
                course.setImageData(image.getBytes());
            }

            var created = courseService.createCourse(course, files);
            var responseDTO = CourseMapper.toResponseDTO(created);

            // ✅ Create and send notification here
            var categoryName = created.getCategory().name();  // or .toString()
            var notification = new NotificationMessage(
                    created.getId(),
                    "📢 New course \"" + created.getTitle() + "\" is now available in " + categoryName + "!",
                    categoryName
            );

            log.info("🔔 Sending real-time notification: {}", notification);
            messagingTemplate.convertAndSend("/topic/category/" + categoryName, notification);

            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Failed to create course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Course creation failed: " + e.getMessage());
        }
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseResponseDTO> updateCourse(
            @PathVariable Long id,
            @RequestPart("course") CourseDTO courseDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        try {
            var course = CourseMapper.toDomain(courseDTO);
            if (image != null && !image.isEmpty()) {
                course.setImageData(image.getBytes());
            }
            return courseService
                    .updateCourse(id, course, files)
                    .map(CourseMapper::toResponseDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Failed to update course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
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
