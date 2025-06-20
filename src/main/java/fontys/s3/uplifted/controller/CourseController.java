package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.business.UserService;
import fontys.s3.uplifted.business.impl.exception.CourseNotFoundException;
import fontys.s3.uplifted.business.impl.mapper.CourseMapper;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.domain.dto.CourseDTO;
import fontys.s3.uplifted.domain.dto.CourseResponseDTO;
import fontys.s3.uplifted.domain.dto.NotificationMessage;
import fontys.s3.uplifted.websocket.NotificationWebSocketEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

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

    @GetMapping("/filter")
    public ResponseEntity<Page<CourseResponseDTO>> getCoursesFiltered(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(courseService.getFilteredCourses(title, category, sort, page));
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<CourseResponseDTO> createCourse(
            @RequestPart("course") CourseDTO courseDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            User instructor = userService.getUserByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

            courseDTO.setInstructorId(instructor.getId());
            var course = CourseMapper.toDomain(courseDTO);

            if (image != null && !image.isEmpty()) {
                course.setImageData(image.getBytes());
            }

            var created = courseService.createCourse(course, files);

            NotificationWebSocketEndpoint.broadcast(new NotificationMessage(
                    created.getId(),
                    created.getTitle() + " is now available!",
                    created.getCategory().name()
            ));

            var responseDTO = CourseMapper.toResponseDTO(created);
            return ResponseEntity.ok(responseDTO);
        } catch (IOException e) {
            log.error("Failed to create course", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseResponseDTO> updateCourse(
            @PathVariable Long id,
            @RequestPart("course") CourseDTO courseDTO,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            User instructor = userService.getUserByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

            var existing = courseService.getCourseById(id)
                    .orElseThrow(() -> new CourseNotFoundException("Course not found"));

            if (!existing.getInstructorId().equals(instructor.getId())) {
                log.warn("Forbidden update attempt by user {} on course {}", instructor.getId(), id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            courseDTO.setInstructorId(instructor.getId());

            var course = CourseMapper.toDomain(courseDTO);
            if (image != null && !image.isEmpty()) {
                course.setImageData(image.getBytes());
            }

            return courseService
                    .updateCourse(id, course, files)
                    .map(CourseMapper::toResponseDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IOException e) {
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
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId,
                                                 Authentication authentication) {
        try {
            String email = authentication.getName();
            courseService.enrollInCourse(courseId, email);
            return ResponseEntity.ok("Enrolled successfully");
        } catch (RuntimeException e) {
            log.warn("Enrollment failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Enrollment error", e);
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @DeleteMapping("/{courseId}/enroll")
    public ResponseEntity<String> unenrollFromCourse(@PathVariable Long courseId,
                                                     Authentication authentication) {
        try {
            String email = authentication.getName();
            courseService.unenrollFromCourse(courseId, email);
            return ResponseEntity.ok("Unenrolled successfully");
        } catch (RuntimeException e) {
            log.warn("Unenrollment failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
