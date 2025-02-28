package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.UserService;
import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers()
                .stream()
                .map(UserMapper::convert)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.getUserById(id).map(UserMapper::convert);
    }

    public User createUser(User user) {
        UserEntity entity = UserMapper.convertToEntity(user);
        UserEntity savedEntity = userRepository.createUser(entity);
        return UserMapper.convert(savedEntity);
    }

    public Optional<User> updateUser(Long id, User user) {
        UserEntity entity = UserMapper.convertToEntity(user);
        return userRepository.updateUser(id, entity).map(UserMapper::convert);
    }

    public boolean deleteUser(Long id) {
        return userRepository.deleteUser(id);
    }
}
