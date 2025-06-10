package fontys.s3.uplifted.E2E;

import fontys.s3.uplifted.UpliftEdApplication;
import fontys.s3.uplifted.config.TestSecurityConfig;
import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.enums.InterestCategory;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import fontys.s3.uplifted.domain.enums.Role;

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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = UpliftEdApplication.class
)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CourseE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity testInstructor;

    @BeforeEach
    void setup() {
        courseRepository.deleteAll();
        userRepository.deleteAll();

        testInstructor = userRepository.save(UserEntity.builder()
                .username("e2e_teacher")
                .email("e2e@uplifted.com")
                .password("encodedpass")
                .firstName("Test")
                .lastName("Teacher")
                .role(Role.TEACHER)
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .joinedDate(LocalDate.now())
                .isActive(true)
                .build());

        courseRepository.save(CourseEntity.builder()
                .title("E2E Course")
                .description("This is an end-to-end test course")
                .instructor(testInstructor)
                .published(true)
                .enrollmentLimit(100)
                .category(InterestCategory.PROGRAMMING)
                .rating(4.5)
                .numberOfReviews(3)
                .build());
    }

    @Test
    void getCourses_shouldReturnPublishedCourses() {
        String url = "http://localhost:" + port + "/api/courses";
        ResponseEntity<String> rawResponse = restTemplate.getForEntity(url, String.class);
        System.out.println("Raw response: " + rawResponse.getBody());
        System.out.println("Status: " + rawResponse.getStatusCode());

        assertThat(rawResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
