package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.StudentAnswer;
import fontys.s3.uplifted.persistence.entity.StudentAnswerEntity;

public final class StudentAnswerMapper {

    public static StudentAnswer toDomain(StudentAnswerEntity entity) {
        return StudentAnswer.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .quizId(entity.getQuizId())
                .questionId(entity.getQuestionId())
                .submittedAnswer(entity.getSubmittedAnswer())
                .awardedMarks(entity.getAwardedMarks())
                .mentorFeedback(entity.getMentorFeedback())
                .reviewed(entity.isReviewed())
                .build();
    }

    public static StudentAnswerEntity toEntity(StudentAnswer answer) {
        return StudentAnswerEntity.builder()
                .id(answer.getId())
                .userId(answer.getUserId())
                .quizId(answer.getQuizId())
                .questionId(answer.getQuestionId())
                .submittedAnswer(answer.getSubmittedAnswer())
                .reviewed(answer.isReviewed())
                .mentorFeedback(answer.getMentorFeedback())
                .awardedMarks(answer.getAwardedMarks())
                .build();
    }
}

