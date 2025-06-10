package fontys.s3.uplifted.E2E;

import fontys.s3.uplifted.UpliftEdApplication;
import fontys.s3.uplifted.config.TestSecurityConfig;
import fontys.s3.uplifted.domain.dto.LoginRequestDTO;
import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UpliftEdApplication.class
)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class AuthE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        userRepository.save(UserEntity.builder()
                .username("testuser")
                .email("testuser@example.com")
                .password(encoder.encode("password123"))
                .firstName("Test")
                .lastName("User")
                .role(Role.STUDENT)
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .joinedDate(LocalDate.now())
                .isActive(true)
                .build()
        );
    }

    @Test
    void login_shouldReturnJwtTokenAndUserDetails() {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("password123");

        String url = "http://localhost:" + port + "/api/auth/login";
        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, loginRequest, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<String, Object> body = response.getBody();
        assertThat(body).isNotNull()
                .containsKeys("user", "token");

        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) body.get("user");
        String token = (String) body.get("token");

        assertThat(user.get("email")).isEqualTo("testuser@example.com");
        assertThat(user.get("username")).isEqualTo("testuser");
        assertThat(user.get("role")).isEqualTo("STUDENT");
        assertThat(token).isNotBlank();
    }
}
