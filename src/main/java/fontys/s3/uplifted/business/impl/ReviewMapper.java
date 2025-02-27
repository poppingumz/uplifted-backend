package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.domain.Review;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.ReviewEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;

final class ReviewMapper {

    private ReviewMapper() {
    }

    public static Review toDomain(ReviewEntity entity) {
        return Review.builder()
                .id(entity.getId())
                .courseId(entity.getCourse().getId())
                .userId(entity.getUser().getId())
                .reviewText(entity.getReviewText())
                .rating(entity.getRating())
                .build();
    }

    public static ReviewEntity toEntity(Review domain, CourseEntity course, UserEntity user) {
        return ReviewEntity.builder()
                .id(domain.getId())
                .course(course)
                .user(user)
                .reviewText(domain.getReviewText())
                .rating(domain.getRating())
                .build();
    }
}
