package fontys.s3.uplifted.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fontys.s3.uplifted.business.impl.UserServiceImpl;
import fontys.s3.uplifted.config.TestSecurityConfig;
import fontys.s3.uplifted.config.security.JwtUtil;
import fontys.s3.uplifted.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setPassword("password123");
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(validUser));
        when(jwtUtil.generateToken("test@example.com")).thenReturn("fake-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        User wrongPasswordUser = new User();
        wrongPasswordUser.setEmail("test@example.com");
        wrongPasswordUser.setPassword("wrongpass");

        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(validUser));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongPasswordUser)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        when(userService.getUserByEmail("notfound@example.com")).thenReturn(Optional.empty());

        User loginUser = new User();
        loginUser.setEmail("notfound@example.com");
        loginUser.setPassword("somepass");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginUser)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }

    @Test
    void testRegisterSuccess() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(validUser);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testRegisterFailure() throws Exception {
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Registration failed: Database error"));
    }
}
