package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.persistence.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<UserEntity> getAllUsers();
    Optional<UserEntity> getUserById(Long id);
    UserEntity createUser(UserEntity user);
    Optional<UserEntity> updateUser(Long id, UserEntity updatedUser);
    boolean deleteUser(Long id);
}
