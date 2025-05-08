package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.User;
import fontys.s3.uplifted.persistence.entity.UserEntity;

public final class UserMapper {

    private UserMapper() {}

    public static User convert(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .dateOfBirth(entity.getDateOfBirth())
                .profileImage(entity.getProfileImage())
                .bio(entity.getBio())
                .joinedDate(entity.getJoinedDate())
                .isActive(entity.isActive())
                .build();
    }

    public static UserEntity convertToEntity(User domain) {
        return UserEntity.builder()
                .id(domain.getId())
                .username(domain.getUsername())
                .email(domain.getEmail())
                .password(domain.getPassword())
                .role(domain.getRole())
                .firstName(domain.getFirstName())
                .lastName(domain.getLastName())
                .dateOfBirth(domain.getDateOfBirth())
                .profileImage(domain.getProfileImage())
                .bio(domain.getBio())
                .joinedDate(domain.getJoinedDate() != null ? domain.getJoinedDate() : java.time.LocalDate.now())
                .isActive(domain.isActive())
                .build();
    }
}
