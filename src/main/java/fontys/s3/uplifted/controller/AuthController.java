package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.impl.UserServiceImpl;
import fontys.s3.uplifted.config.security.JwtUtil;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.domain.dto.LoginRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        return userService.getUserByEmail(loginRequest.getEmail())
                .map(u -> {
                    if (u.getPassword() == null || loginRequest.getPassword() == null) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Missing password"));
                    }

                    if (!passwordEncoder.matches(loginRequest.getPassword(), u.getPassword())) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
                    }

                    try {
                        String token = jwtUtil.generateToken(u);
                        Map<String, Object> userResponse = Map.ofEntries(
                                Map.entry("id", u.getId()),
                                Map.entry("email", u.getEmail()),
                                Map.entry("username", u.getUsername()),
                                Map.entry("role", u.getRole()),
                                Map.entry("firstName", u.getFirstName()),
                                Map.entry("lastName", u.getLastName()),
                                Map.entry("dateOfBirth", u.getDateOfBirth() != null ? u.getDateOfBirth() : ""),
                                Map.entry("profileImage", u.getProfileImage() != null ? u.getProfileImage() : ""),
                                Map.entry("bio", u.getBio() != null ? u.getBio() : ""),
                                Map.entry("joinedDate", u.getJoinedDate() != null ? u.getJoinedDate() : ""),
                                Map.entry("active", u.isActive())
                        );

                        return ResponseEntity.ok(Map.of("user", userResponse, "token", token));
                    } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(Map.of("error", "Internal server error: " + e.getMessage()));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User created = userService.createUser(user);
            created.setPassword(null);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Registration failed: " + ex.getMessage());
        }
    }
}
