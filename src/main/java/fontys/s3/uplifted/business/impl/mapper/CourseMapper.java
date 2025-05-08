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
                .imageData(entity.getImageData())
                .instructorId(entity.getInstructor() != null ? entity.getInstructor().getId() : null)
                .enrolledStudentIds(entity.getEnrolledStudents() != null
                        ? entity.getEnrolledStudents().stream()
                        .map(UserEntity::getId)
                        .collect(Collectors.toSet())
                        : Set.of())
                .enrollmentLimit(entity.getEnrollmentLimit())
                .published(entity.isPublished())
                .category(entity.getCategory())
                .rating(entity.getRating())
                .numberOfReviews(entity.getNumberOfReviews())
                .build();
    }


    public static CourseEntity toEntity(Course domain, UserEntity instructor) {
        return CourseEntity.builder()
                .id(domain.getId())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .imageData(domain.getImageData())
                .instructor(instructor)
                .enrollmentLimit(domain.getEnrollmentLimit())
                .published(domain.isPublished())
                .category(domain.getCategory())
                .rating(domain.getRating())
                .numberOfReviews(domain.getNumberOfReviews())
                .build();
    }
}
