package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.Question;
import fontys.s3.uplifted.persistence.entity.QuestionEntity;
import fontys.s3.uplifted.persistence.entity.QuizEntity;

import java.util.stream.Collectors;

public final class QuestionMapper {

    public static Question toDomain(QuestionEntity entity) {
        return Question.builder()
                .id(entity.getId())
                .text(entity.getText())
                .type(entity.getType())
                .marks(entity.getMarks())
                .correctAnswer(entity.getCorrectAnswer())
                .requiresReview(entity.isRequiresReview())
                .answers(entity.getAnswers().stream()
                        .map(AnswerMapper::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public static QuestionEntity toEntity(Question question, QuizEntity quiz) {
        return QuestionEntity.builder()
                .id(question.getId())
                .quiz(quiz)
                .text(question.getText())
                .type(question.getType())
                .marks(question.getMarks())
                .correctAnswer(question.getCorrectAnswer())
                .requiresReview(question.isRequiresReview())
                .build();
    }
}
