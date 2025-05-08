package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find user by email")
    void testFindByEmail() {
        UserEntity user = UserEntity.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password")
                .role(Role.STUDENT)
                .firstName("Test")
                .lastName("User")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .joinedDate(LocalDate.now())
                .isActive(true)
                .build();

        testEntityManager.persistAndFlush(user);

        Optional<UserEntity> result = userRepository.findByEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("Find user by username")
    void testFindByUsername() {
        UserEntity user = UserEntity.builder()
                .username("uniqueuser")
                .email("unique@example.com")
                .password("password")
                .role(Role.TEACHER)
                .firstName("Unique")
                .lastName("User")
                .dateOfBirth(LocalDate.of(1995, 5, 5))
                .joinedDate(LocalDate.now())
                .isActive(true)
                .build();

        testEntityManager.persistAndFlush(user);

        Optional<UserEntity> result = userRepository.findByUsername("uniqueuser");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("unique@example.com");
    }
}

