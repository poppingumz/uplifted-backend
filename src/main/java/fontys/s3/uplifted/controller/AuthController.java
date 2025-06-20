package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.impl.UserServiceImpl;
import fontys.s3.uplifted.config.security.JwtUtil;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.domain.dto.LoginRequestDTO;
import fontys.s3.uplifted.domain.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequestDTO loginRequest) {
        log.info("Attempting login for: {}", loginRequest.getEmail());

        // 1) Fetch user
        var optUser = userService.getUserByEmail(loginRequest.getEmail());
        if (optUser.isEmpty()) {
            log.warn("User not found: {}", loginRequest.getEmail());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }

        var u = optUser.get();

        // 2) Check passwords present
        if (u.getPassword() == null || loginRequest.getPassword() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Missing password");
        }

        // 3) Validate password
        boolean matches = passwordEncoder.matches(loginRequest.getPassword(), u.getPassword());
        log.debug("Password match: {}", matches);
        if (!matches) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
        }

        // 4) Generate token and build DTO
        try {
            String token = jwtUtil.generateToken(u);
            LoginResponseDTO response = LoginResponseDTO.builder()
                    .id(u.getId())
                    .email(u.getEmail())
                    .username(u.getUsername())
                    .role(u.getRole())
                    .firstName(u.getFirstName())
                    .lastName(u.getLastName())
                    .dateOfBirth(u.getDateOfBirth())
                    .profileImage(u.getProfileImage())
                    .bio(u.getBio())
                    .joinedDate(u.getJoinedDate())
                    .active(u.isActive())
                    .token(token)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Token generation failed", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }



    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        try {
            User created = userService.createUser(user);
            created.setPassword(null); // Never expose passwords
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            log.warn("Registration failed: {}", ex.getMessage());
            return ResponseEntity.badRequest().body("Registration failed: " + ex.getMessage());
        }
    }


    @GetMapping("/account")
    public ResponseEntity<User> getAccountDetails(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserByEmail(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.FORBIDDEN).build());
    }
}
