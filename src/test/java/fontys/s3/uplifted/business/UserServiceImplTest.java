package fontys.s3.uplifted.business;

import fontys.s3.uplifted.business.impl.UserServiceImpl;
import fontys.s3.uplifted.business.impl.exception.UserServiceException;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.domain.enums.Role;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(sampleEntity));

        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("johndoe", users.get(0).getUsername());
    }

    @Test
    void testGetAllUsersThrowsException() {
        when(userRepository.findAll()).thenThrow(new RuntimeException("DB error"));
        assertThrows(UserServiceException.class, () -> userService.getAllUsers());
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));

        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals("johndoe", result.get().getUsername());
    }

    @Test
    void testGetUserByIdThrowsException() {
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));
        assertThrows(UserServiceException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testGetUserByEmail() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleEntity));

        Optional<User> user = userService.getUserByEmail("john@example.com");
        assertTrue(user.isPresent());
        assertEquals("johndoe", user.get().getUsername());
    }

    @Test
    void testGetUserByEmailThrowsException() {
        when(userRepository.findByEmail("john@example.com")).thenThrow(new RuntimeException("DB error"));
        assertThrows(UserServiceException.class, () -> userService.getUserByEmail("john@example.com"));
    }

    @Test
    void testCreateUser() {
        when(passwordEncoder.encode(sampleUser.getPassword())).thenReturn("hashedpassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(sampleEntity);

        User created = userService.createUser(sampleUser);

        assertNotNull(created);
        assertEquals("johndoe", created.getUsername());
        verify(passwordEncoder).encode("securepassword");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testCreateUserThrowsException() {
        when(passwordEncoder.encode(sampleUser.getPassword())).thenReturn("hashedpassword");
        when(userRepository.save(any(UserEntity.class))).thenThrow(new RuntimeException("DB error"));
        assertThrows(UserServiceException.class, () -> userService.createUser(sampleUser));
    }

    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
        when(passwordEncoder.encode(sampleUser.getPassword())).thenReturn("hashedpassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(sampleEntity);

        Optional<User> updated = userService.updateUser(1L, sampleUser);

        assertTrue(updated.isPresent());
        assertEquals("johndoe", updated.get().getUsername());
        verify(passwordEncoder).encode("securepassword");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void testUpdateUserThrowsException() {
        when(userRepository.findById(1L)).thenThrow(new RuntimeException("DB error"));
        assertThrows(UserServiceException.class, () -> userService.updateUser(1L, sampleUser));
    }

    @Test
    void testDeleteUserSuccess() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        boolean result = userService.deleteUser(1L);
        assertTrue(result);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(2L)).thenReturn(false);

        boolean result = userService.deleteUser(2L);
        assertFalse(result);
    }

    @Test
    void testDeleteUserThrowsException() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doThrow(new RuntimeException("DB error")).when(userRepository).deleteById(1L);

        assertThrows(UserServiceException.class, () -> userService.deleteUser(1L));
    }
}
