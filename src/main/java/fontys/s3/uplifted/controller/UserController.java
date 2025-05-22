package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.CourseService;
import fontys.s3.uplifted.business.UserService;
import fontys.s3.uplifted.business.impl.CourseServiceImpl;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.business.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final CourseService courseService;


    public UserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            log.error("Failed to retrieve users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, Authentication authentication) {
        try {
            String emailFromToken = authentication.getName();

            Optional<User> userOpt = userService.getUserByEmail(emailFromToken);

            if (userOpt.isEmpty()) {
                log.warn("User with email {} not found", emailFromToken);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = userOpt.get();

            if (!user.getId().equals(id)) {
                log.warn("User {} attempted to access user {}", user.getId(), id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            return ResponseEntity.ok(user);

        } catch (Exception e) {
            log.error("Failed to retrieve user with ID {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestPart("user") User user,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) {
        try {
            if (profileImage != null && !profileImage.isEmpty()) {
                user.setProfileImage(profileImage.getBytes());
            }
            return userService.updateUser(id, user)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("User with ID {} not found for update", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Failed to update user with ID {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (deleted) {
                log.info("User with ID {} deleted successfully", id);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("User with ID {} not found for deletion", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Failed to delete user with ID {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}/created-courses")
    public ResponseEntity<List<Course>> getCreatedCourses(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCoursesByInstructor(id));
    }

    @GetMapping("/{id}/enrolled-courses")
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCoursesByEnrolledUser(id));
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/{id}/profile-image")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id, Authentication authentication) {
        String emailFromToken = authentication.getName();
        Optional<User> requestingUser = userService.getUserByEmail(emailFromToken);

        if (requestingUser.isEmpty() || !requestingUser.get().getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> targetUser = userService.getUserById(id);
        if (targetUser.isEmpty() || targetUser.get().getProfileImage() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes = targetUser.get().getProfileImage();
        String contentType = "application/octet-stream";

        try {
            contentType = java.nio.file.Files.probeContentType(
                    java.nio.file.Paths.get("dummy." + detectImageExtension(imageBytes))
            );
        } catch (Exception e) {
            log.warn("Could not detect content type", e);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(imageBytes);
    }


    private String detectImageExtension(byte[] imageBytes) {
        if (imageBytes.length >= 4) {
            // JPEG
            if ((imageBytes[0] & 0xFF) == 0xFF && (imageBytes[1] & 0xFF) == 0xD8) {
                return "jpg";
            }
            // PNG
            if ((imageBytes[0] & 0xFF) == 0x89 && (imageBytes[1] & 0xFF) == 0x50) {
                return "png";
            }
            // WEBP
            if ((char) imageBytes[8] == 'W' && (char) imageBytes[9] == 'E') {
                return "webp";
            }
            // GIF
            if ((char) imageBytes[0] == 'G' && (char) imageBytes[1] == 'I') {
                return "gif";
            }
        }
        return "bin";
    }

}
