package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFetchCourse() {
        UserEntity instructor = UserEntity.builder()
                .username("teacher1")
                .email("teacher@example.com")
                .password("pass")
                .role(Role.TEACHER)
                .build();

        UserEntity savedInstructor = userRepository.save(instructor);

        CourseEntity course = CourseEntity.builder()
                .title("Spring Boot")
                .description("Learn Spring")
                .instructor(savedInstructor)
                .enrolledStudents(Collections.emptySet())
                .enrollmentLimit(50)
                .published(true)
                .category("Programming")
                .build();

        courseRepository.save(course);

        List<CourseEntity> courses = courseRepository.findAll();
        assertFalse(courses.isEmpty());
        assertEquals("Spring Boot", courses.get(0).getTitle());
    }
}
