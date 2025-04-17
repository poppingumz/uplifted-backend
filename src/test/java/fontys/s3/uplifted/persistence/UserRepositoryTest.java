package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserByEmail() {
        UserEntity user = UserEntity.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .role(Role.STUDENT)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        userRepository.save(user);

        Optional<UserEntity> found = userRepository.findByEmail("test@example.com");
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    void shouldReturnEmptyWhenEmailNotExists() {
        Optional<UserEntity> user = userRepository.findByEmail("nonexistent@example.com");
        assertTrue(user.isEmpty());
    }
}
