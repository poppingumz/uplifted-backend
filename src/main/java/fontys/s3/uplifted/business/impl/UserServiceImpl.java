// UserServiceImpl.java
package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.UserService;
import fontys.s3.uplifted.business.impl.mapper.UserMapper;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return userRepository.findAll()
                    .stream()
                    .map(UserMapper::convert)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            throw new RuntimeException("Unable to retrieve users.");
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try {
            return userRepository.findById(id).map(UserMapper::convert);
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            throw new RuntimeException("Unable to retrieve user.");
        }
    }

    @Override
    public User createUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            UserEntity entity = UserMapper.convertToEntity(user);
            UserEntity savedEntity = userRepository.save(entity);
            log.info("User created with ID: {}", savedEntity.getId());
            return UserMapper.convert(savedEntity);
        } catch (Exception e) {
            log.error("Error creating user", e);
            throw new RuntimeException("Unable to create user.");
        }
    }

    @Override
    public Optional<User> updateUser(Long id, User user) {
        try {
            return userRepository.findById(id).map(existing -> {
                if (user.getPassword() != null && !user.getPassword().isBlank()) {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                }
                UserEntity entity = UserMapper.convertToEntity(user);
                entity.setId(id);
                UserEntity updated = userRepository.save(entity);
                return UserMapper.convert(updated);
            });
        } catch (Exception e) {
            log.error("Error updating user with ID: {}", id, e);
            throw new RuntimeException("Unable to update user.");
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        try {
            if (!userRepository.existsById(id)) {
                log.warn("No user found with ID {} to delete", id);
                return false;
            }
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}", id, e);
            throw new RuntimeException("Unable to delete user.");
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::convert);
    }


}