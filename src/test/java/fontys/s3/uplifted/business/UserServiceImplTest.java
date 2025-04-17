
package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.UserServiceImpl;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity sampleEntity;
    private User sampleUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleEntity = UserEntity.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .password("securepassword")
                .role(Role.STUDENT)
                .build();

        sampleUser = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .password("securepassword")
                .role(Role.STUDENT)
                .build();
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(sampleEntity));

        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("johndoe", users.get(0).getUsername());
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));

        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals("johndoe", result.get().getUsername());
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(sampleEntity);

        User created = userService.createUser(sampleUser);
        assertNotNull(created);
        assertEquals("johndoe", created.getUsername());
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(sampleEntity);

        Optional<User> updated = userService.updateUser(1L, sampleUser);
        assertTrue(updated.isPresent());
        assertEquals("johndoe", updated.get().getUsername());
    }

    @Test
    public void testDeleteUserSuccess() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        boolean result = userService.deleteUser(1L);
        assertTrue(result);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.existsById(2L)).thenReturn(false);

        boolean result = userService.deleteUser(2L);
        assertFalse(result);
    }

    @Test
    public void testGetUserByEmail() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleEntity));

        Optional<User> user = userService.getUserByEmail("john@example.com");
        assertTrue(user.isPresent());
        assertEquals("johndoe", user.get().getUsername());
    }
}
