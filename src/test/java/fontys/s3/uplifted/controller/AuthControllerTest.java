package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.impl.UserServiceImpl;
import fontys.s3.uplifted.config.security.JwtUtil;
import fontys.s3.uplifted.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    private User validUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setPassword("password123");
    }

    @Test
    void shouldLoginSuccess() {
        User userFromDb = new User();
        userFromDb.setEmail("test@example.com");
        userFromDb.setPassword("encoded-password");

        when(userService.getUserByEmail(validUser.getEmail()))
                .thenReturn(Optional.of(userFromDb));
        when(passwordEncoder.matches("password123", "encoded-password"))
                .thenReturn(true);
        when(jwtUtil.generateToken(validUser.getEmail()))
                .thenReturn("fake-jwt-token");

        validUser.setPassword("password123");

        ResponseEntity<?> response = authController.login(validUser);

        assertEquals(200, response.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("fake-jwt-token", body.get("token"));
        assertNotNull(body.get("user"));

        verify(userService).getUserByEmail("test@example.com");
        verify(passwordEncoder).matches("password123", "encoded-password");
        verify(jwtUtil).generateToken("test@example.com");
    }

    @Test
    void shouldLoginInvalidPassword() {
        when(userService.getUserByEmail(validUser.getEmail()))
                .thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("wrongpass", validUser.getPassword()))
                .thenReturn(false);

        User wrongUser = new User();
        wrongUser.setEmail(validUser.getEmail());
        wrongUser.setPassword("wrongpass");

        ResponseEntity<?> response = authController.login(wrongUser);

        assertEquals(401, response.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Invalid email or password", body.get("error"));

        verify(userService).getUserByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongpass", validUser.getPassword());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void shouldLoginUserNotFound() {
        when(userService.getUserByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        User missing = new User();
        missing.setEmail("notfound@example.com");
        missing.setPassword("irrelevant");

        ResponseEntity<?> response = authController.login(missing);

        assertEquals(401, response.getStatusCodeValue());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Invalid email or password", body.get("error"));

        verify(userService).getUserByEmail("notfound@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void shouldRegisterSuccess() {
        when(userService.createUser(validUser))
                .thenReturn(validUser);

        ResponseEntity<?> response = authController.register(validUser);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(validUser, response.getBody());
        verify(userService).createUser(validUser);
    }

    @Test
    void shouldRegisterFailure() {
        when(userService.createUser(validUser))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<?> response = authController.register(validUser);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Registration failed: Database error", response.getBody());
        verify(userService).createUser(validUser);
    }
}
