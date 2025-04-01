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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserServiceImpl userService;

    private UserEntity userEntity;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = new UserEntity(1L, "testUser", "test@example.com", "password123", Role.STUDENT, null);
        user = new User(1L, "testUser", "test@example.com", "password123", Role.STUDENT);
    }

    @Test
    void testGetAllUsers() {
        //Tester
        when(userRepository.getAllUsers()).thenReturn(List.of(userEntity));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.getUserById(1L)).thenReturn(Optional.of(userEntity));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.getUserById(999L)).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(999L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testCreateUser() {
        when(userRepository.createUser(any(UserEntity.class))).thenReturn(userEntity);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("testUser", createdUser.getUsername());
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.updateUser(eq(1L), any(UserEntity.class))).thenReturn(Optional.of(userEntity));

        Optional<User> updatedUser = userService.updateUser(1L, user);

        assertTrue(updatedUser.isPresent());
        assertEquals("testUser", updatedUser.get().getUsername());
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.updateUser(eq(999L), any(UserEntity.class))).thenReturn(Optional.empty());

        Optional<User> updatedUser = userService.updateUser(999L, user);

        assertFalse(updatedUser.isPresent());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.deleteUser(1L)).thenReturn(true);

        boolean result = userService.deleteUser(1L);

        assertTrue(result);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.deleteUser(999L)).thenReturn(false);

        boolean result = userService.deleteUser(999L);

        assertFalse(result);
    }
}
