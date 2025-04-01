package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.persistence.entity.QuizEntity;

public final class QuizMapper {
    public static Quiz toDomain(QuizEntity entity) {
        return Quiz.builder()
                .id(entity.getId())
                .courseId(entity.getCourseId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .totalMarks(entity.getTotalMarks())
                .passingMarks(entity.getPassingMarks())
                .questionsAndAnswers(entity.getQuestionsAndAnswers())
                .build();
    }

    public static QuizEntity toEntity(Quiz quiz) {
        return QuizEntity.builder()
                .id(quiz.getId())
                .courseId(quiz.getCourseId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .totalMarks(quiz.getTotalMarks())
                .passingMarks(quiz.getPassingMarks())
                .questionsAndAnswers(quiz.getQuestionsAndAnswers())
                .build();
    }
}
