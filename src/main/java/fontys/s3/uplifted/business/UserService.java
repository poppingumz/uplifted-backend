package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User createUser(User user);
    Optional<User> updateUser(Long id, User user);
    boolean deleteUser(Long id);
    Optional<User> getUserByEmail(String email);
}
