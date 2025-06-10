package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.domain.enums.InterestCategory;
import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
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
public class QuizRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuizRepository quizRepository;

    @Test
    @DisplayName("Save and find quiz by course ID")
    void testFindByCourseId() {
        UserEntity instructor = UserEntity.builder()
                .username("quiz_creator")
                .email("quiz@creator.com")
                .password("secure123")
                .role(Role.TEACHER)
                .firstName("Quiz")
                .lastName("Maker")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .bio("I make quizzes.")
                .joinedDate(LocalDate.now())
                .isActive(true)
                .build();
        entityManager.persist(instructor);

        CourseEntity course = CourseEntity.builder()
                .title("DSA")
                .description("Data Structures & Algorithms")
                .instructor(instructor)
                .enrollmentLimit(50)
                .published(true)
                .category(InterestCategory.PROGRAMMING)
                .rating(5.0)
                .numberOfReviews(0)
                .build();
        entityManager.persist(course);

        QuizEntity quiz = QuizEntity.builder()
                .title("DSA Quiz")
                .description("Test your DSA knowledge")
                .totalMarks(100)
                .passingMarks(60)
                .course(course)
                .createdBy(instructor)
                .build();
        quizRepository.save(quiz);

        List<QuizEntity> result = quizRepository.findByCourseId(course.getId());

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getTitle()).isEqualTo("DSA Quiz");
        assertThat(result.get(0).getCourse().getTitle()).isEqualTo("DSA");
    }
}
