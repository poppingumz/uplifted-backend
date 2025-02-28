package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.domain.Enrollment;
import fontys.s3.uplifted.persistence.entity.EnrollmentEntity;

final class EnrollmentMapper {
    private EnrollmentMapper() {}

    public static Enrollment convert(EnrollmentEntity entity) {
        return Enrollment.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .courseId(entity.getCourseId())
                .enrollmentDate(entity.getEnrollmentDate())
                .build();
    }

    public static EnrollmentEntity convertToEntity(Enrollment enrollment) {
        return EnrollmentEntity.builder()
                .id(enrollment.getId())
                .userId(enrollment.getUserId())
                .courseId(enrollment.getCourseId())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .build();
    }
}
