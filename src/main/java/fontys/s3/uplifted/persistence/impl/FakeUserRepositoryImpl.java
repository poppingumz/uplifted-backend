package fontys.s3.uplifted.persistence.impl;

import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeUserRepositoryImpl implements UserRepository {
    private final Map<Long, UserEntity> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<UserEntity> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<UserEntity> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        long newId = idGenerator.getAndIncrement();
        user.setId(newId);
        users.put(newId, user);
        return user;
    }

    @Override
    public Optional<UserEntity> updateUser(Long id, UserEntity updatedUser) {
        if (!users.containsKey(id)) {
            return Optional.empty();
        }
        updatedUser.setId(id);
        users.put(id, updatedUser);
        return Optional.of(updatedUser);
    }

    @Override
    public boolean deleteUser(Long id) {
        return users.remove(id) != null;
    }
}
