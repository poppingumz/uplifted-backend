package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.persistence.entity.CourseEntity;

final class CourseMapper {
    private CourseMapper() {
    }

    public static Course toDomain(CourseEntity entity) {
        return Course.builder()
                .id(entity.getId())
                .teacherId(entity.getTeacherId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .enrolledStudentIds(entity.getEnrolledStudentIds())
                .build();
    }

    public static CourseEntity toEntity(Course domain) {
        return CourseEntity.builder()
                .id(domain.getId())
                .teacherId(domain.getTeacherId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .enrolledStudentIds(domain.getEnrolledStudentIds())
                .build();
    }
}
