package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;

import java.util.Set;
import java.util.stream.Collectors;

public final class CourseMapper {
    private CourseMapper() {}

    public static Course toDomain(CourseEntity entity) {
        return Course.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .instructorId(entity.getInstructor().getId())
                .enrolledStudentIds(entity.getEnrolledStudents() != null
                        ? entity.getEnrolledStudents().stream()
                        .map(UserEntity::getId)
                        .collect(Collectors.toSet())
                        : Set.of())
                .build();
    }

    public static CourseEntity toEntity(Course domain) {
        return CourseEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .build();
    }
}
