package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.business.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            return userServiceImpl.getUserById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("User with ID {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Failed to retrieve user with ID {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User createdUser = userServiceImpl.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            log.error("Failed to create user", e);
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
