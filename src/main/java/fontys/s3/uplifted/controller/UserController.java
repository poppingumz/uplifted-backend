package fontys.s3.uplifted.controller;

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

    private final UserServiceImpl userServiceImpl;

    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userServiceImpl.getAllUsers());
        } catch (Exception e) {
            log.error("Failed to retrieve users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id, Authentication authentication) {
        try {
            String emailFromToken = authentication.getName();

            Optional<User> userOpt = userServiceImpl.getUserByEmail(emailFromToken);

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
        return userServiceImpl.getUserByEmail(email)
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
            return userServiceImpl.updateUser(id, user)
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

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            return userServiceImpl.updateUser(id, user)
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
            boolean deleted = userServiceImpl.deleteUser(id);
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
}
