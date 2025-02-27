package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.persistence.entity.CourseEntity;

final class CourseMapper {
    private CourseMapper() {
    }

    public static Course toDomain(CourseEntity entity) {
        return Course.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .instructorName(entity.getInstructorName())
                .enrolledStudentIds(entity.getEnrolledStudentIds()) // Fix: Match correct field name
                .build();
    }

    public static CourseEntity toEntity(Course domain) {
        return CourseEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .instructorName(domain.getInstructorName())
                .enrolledStudentIds(domain.getEnrolledStudentIds()) // Fix: Match correct field name
                .build();
    }
}
