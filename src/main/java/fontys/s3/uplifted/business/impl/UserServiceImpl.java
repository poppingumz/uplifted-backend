package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.UserService;
import fontys.s3.uplifted.business.impl.mapper.UserMapper;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.getAllUsers()
                    .stream()
                    .map(UserMapper::convert)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            throw new RuntimeException("Unable to retrieve users.");
        }
    }

    public Optional<User> getUserById(Long id) {
        try {
            return userRepository.getUserById(id).map(UserMapper::convert);
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            throw new RuntimeException("Unable to retrieve user.");
        }
    }

    public User createUser(User user) {
        try {
            UserEntity entity = UserMapper.convertToEntity(user);
            UserEntity savedEntity = userRepository.createUser(entity);
            log.info("User created with ID: {}", savedEntity.getId());
            return UserMapper.convert(savedEntity);
        } catch (Exception e) {
            log.error("Error creating user", e);
            throw new RuntimeException("Unable to create user.");
        }
    }

    public Optional<User> updateUser(Long id, User user) {
        try {
            UserEntity entity = UserMapper.convertToEntity(user);
            return userRepository.updateUser(id, entity).map(UserMapper::convert);
        } catch (Exception e) {
            log.error("Error updating user with ID: {}", id, e);
            throw new RuntimeException("Unable to update user.");
        }
    }

    public boolean deleteUser(Long id) {
        try {
            boolean deleted = userRepository.deleteUser(id);
            if (!deleted) {
                log.warn("No user found with ID {} to delete", id);
            }
            return deleted;
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}", id, e);
            throw new RuntimeException("Unable to delete user.");
        }
    }
}
