package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.impl.UserServiceImpl;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = User.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .password("pass")
                .role(Role.STUDENT)
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();
    }

    @Test
    void getAllUsers_success() {
        when(userService.getAllUsers()).thenReturn(List.of(mockUser));
        ResponseEntity<List<User>> response = userController.getAllUsers();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getUserById_success() {
        when(authentication.getName()).thenReturn("john@example.com");
        when(userService.getUserByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = userController.getUserById(1L, authentication);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockUser, response.getBody());
    }

    @Test
    void getUserById_unauthorized() {
        when(authentication.getName()).thenReturn("invalid@example.com");
        when(userService.getUserByEmail("invalid@example.com")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(1L, authentication);
        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void getUserById_forbidden() {
        User otherUser = User.builder().id(2L).email("john@example.com").build();
        when(authentication.getName()).thenReturn("john@example.com");
        when(userService.getUserByEmail("john@example.com")).thenReturn(Optional.of(otherUser));

        ResponseEntity<User> response = userController.getUserById(1L, authentication);
        assertEquals(403, response.getStatusCodeValue());
    }

    @Test
    void getCurrentUser_success() {
        when(authentication.getName()).thenReturn("john@example.com");
        when(userService.getUserByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = userController.getCurrentUser(authentication);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void updateUser_basic_success() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = userController.updateUser(1L, mockUser, null);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void updateUser_withImage_success() throws Exception {
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "img.jpg", "image/jpeg", new byte[]{1, 2, 3});
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = userController.updateUser(1L, mockUser, profileImage);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void updateUser_notFound() {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(Optional.empty());
        ResponseEntity<User> response = userController.updateUser(1L, mockUser, null);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deleteUser_success() {
        when(userService.deleteUser(1L)).thenReturn(true);
        ResponseEntity<Void> response = userController.deleteUser(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteUser_notFound() {
        when(userService.deleteUser(1L)).thenReturn(false);
        ResponseEntity<Void> response = userController.deleteUser(1L);
        assertEquals(404, response.getStatusCodeValue());
    }
}
