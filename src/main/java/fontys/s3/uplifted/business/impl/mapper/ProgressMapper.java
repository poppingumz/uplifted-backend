package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.Progress;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.ProgressEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;

public final class ProgressMapper {
    private ProgressMapper() {
    }

    public static Progress toDomain(ProgressEntity entity) {
        return Progress.builder()
                .id(entity.getId())
                .courseId(entity.getCourse().getId())
                .userId(entity.getUser().getId())
                .progressPercentage(entity.getProgressPercentage())
                .build();
    }

    public static ProgressEntity toEntity(Progress domain, CourseEntity course, UserEntity user) {
        return ProgressEntity.builder()
                .id(domain.getId())
                .course(course)
                .user(user)
                .progressPercentage(domain.getProgressPercentage())
                .build();
    }
}
