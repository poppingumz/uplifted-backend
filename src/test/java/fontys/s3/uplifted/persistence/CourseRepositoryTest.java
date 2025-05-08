package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    @DisplayName("Save and find by instructor ID")
    void testFindByInstructorId() {
        UserEntity instructor = UserEntity.builder()
                .username("instructor01")
                .email("instructor@example.com")
                .password("securePass")
                .role(Role.TEACHER)
                .firstName("Jane")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .bio("Java expert")
                .joinedDate(LocalDate.now())
                .isActive(true)
                .build();

        instructor = testEntityManager.persistFlushFind(instructor);

        CourseEntity course = CourseEntity.builder()
                .title("Java Fundamentals")
                .description("Learn Java from scratch.")
                .instructor(instructor)
                .enrollmentLimit(50)
                .published(true)
                .category("Programming")
                .rating(4.5)
                .numberOfReviews(10)
                .build();

        courseRepository.save(course);

        List<CourseEntity> result = courseRepository.findByInstructorId(instructor.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo("Java Fundamentals");
    }
}
