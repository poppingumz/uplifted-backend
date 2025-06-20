package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.impl.UserServiceImpl;
import fontys.s3.uplifted.config.security.JwtUtil;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.domain.dto.LoginRequestDTO;
import fontys.s3.uplifted.domain.dto.LoginResponseDTO;
import fontys.s3.uplifted.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock private UserServiceImpl userService;
    @Mock private JwtUtil jwtUtil;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private AuthController authController;

    private User validUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validUser = new User();
        validUser.setId(1L);
        validUser.setEmail("test@example.com");
        validUser.setUsername("tester");
        validUser.setPassword("encodedpass123");
        validUser.setRole(Role.STUDENT);
        validUser.setFirstName("John");
        validUser.setLastName("Doe");
        validUser.setDateOfBirth(LocalDate.of(1990, 1, 1));
        validUser.setActive(true);
        validUser.setBio("bio");
        validUser.setJoinedDate(LocalDate.of(2024, 1, 1));
        validUser.setProfileImage(new byte[]{1, 2, 3});
    }

    @Test
    void shouldLoginSuccess() {
        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("test@example.com");
        login.setPassword("raw-password");

        when(userService.getUserByEmail("test@example.com"))
                .thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("raw-password", "encodedpass123"))
                .thenReturn(true);
        when(jwtUtil.generateToken(validUser)).thenReturn("jwt-token");

        ResponseEntity<Object> response = authController.login(login);

        assertEquals(200, response.getStatusCodeValue());
        // Cast to DTO
        LoginResponseDTO body = (LoginResponseDTO) response.getBody();
        assertNotNull(body);
        assertEquals("jwt-token", body.getToken());
        assertEquals("test@example.com", body.getEmail());
    }

    @Test
    void shouldLoginWithInvalidPassword() {
        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("test@example.com");
        login.setPassword("wrongpass");

        when(userService.getUserByEmail("test@example.com"))
                .thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("wrongpass", "encodedpass123"))
                .thenReturn(false);

        ResponseEntity<Object> response = authController.login(login);

        assertEquals(401, response.getStatusCodeValue());
        String err = (String) response.getBody();
        assertEquals("Invalid credentials", err);
    }

    @Test
    void shouldLoginWhenPasswordMissing() {
        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("test@example.com");
        login.setPassword(null);

        validUser.setPassword(null);
        when(userService.getUserByEmail("test@example.com"))
                .thenReturn(Optional.of(validUser));

        ResponseEntity<Object> response = authController.login(login);

        assertEquals(401, response.getStatusCodeValue());
        String err = (String) response.getBody();
        assertEquals("Missing password", err);
    }

    @Test
    void shouldLoginUserNotFound() {
        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("notfound@example.com");
        login.setPassword("irrelevant");

        when(userService.getUserByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        ResponseEntity<Object> response = authController.login(login);

        assertEquals(401, response.getStatusCodeValue());
        String err = (String) response.getBody();
        assertEquals("Invalid credentials", err);
    }

    @Test
    void shouldLoginTokenFailsInternally() {
        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("test@example.com");
        login.setPassword("raw-password");

        when(userService.getUserByEmail("test@example.com"))
                .thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("raw-password", "encodedpass123"))
                .thenReturn(true);
        when(jwtUtil.generateToken(validUser))
                .thenThrow(new RuntimeException("JWT failure"));

        ResponseEntity<Object> response = authController.login(login);

        assertEquals(500, response.getStatusCodeValue());
        String err = (String) response.getBody();
        assertTrue(err.contains("Internal server error"));
    }

    @Test
    void shouldRegisterSuccess() {
        User input = new User();
        input.setEmail("new@example.com");
        input.setPassword("123");

        User created = new User();
        created.setEmail("new@example.com");

        when(userService.createUser(input)).thenReturn(created);

        ResponseEntity<Object> response = authController.register(input);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(created, response.getBody());
        assertNull(created.getPassword());
    }

    @Test
    void shouldRegisterFailure() {
        when(userService.createUser(validUser))
                .thenThrow(new RuntimeException("duplicate email"));

        ResponseEntity<Object> response = authController.register(validUser);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Registration failed: duplicate email", response.getBody());
    }
}
