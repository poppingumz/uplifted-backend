package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.impl.UserServiceImpl;
import fontys.s3.uplifted.config.security.JwtUtil;
import fontys.s3.uplifted.domain.User;
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
    public ResponseEntity<?> login(@RequestBody User loginRequest) {
        System.out.println("Login attempt: email = " + loginRequest.getEmail() + ", password = " + loginRequest.getPassword());

        return userService.getUserByEmail(loginRequest.getEmail())
                .map(u -> {
                    System.out.println("User found: " + u.getEmail());
                    System.out.println("Stored hash: " + u.getPassword());
                    boolean matches = passwordEncoder.matches(loginRequest.getPassword(), u.getPassword());
                    System.out.println("Password match result: " + matches);

                    if (!matches) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "Wrong password"));
                    }

                    String token = jwtUtil.generateToken(u);

                    u.setPassword(null);
                    return ResponseEntity.ok(Map.of("token", token, "user", u));
                })
                .orElseGet(() -> {
                    System.out.println("No user with email: " + loginRequest.getEmail());
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("error", "Invalid email or password"));
                });
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User created = userService.createUser(user);
            created.setPassword(null);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .badRequest()
                    .body("Registration failed: " + ex.getMessage());
        }
    }
}
