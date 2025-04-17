package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.Answer;
import fontys.s3.uplifted.persistence.entity.AnswerEntity;
import fontys.s3.uplifted.persistence.entity.QuestionEntity;

public final class AnswerMapper {

    public static Answer toDomain(AnswerEntity entity) {
        return Answer.builder()
                .id(entity.getId())
                .text(entity.getText())
                .correct(entity.isCorrect())
                .explanation(entity.getExplanation())
                .build();
    }

    public static AnswerEntity toEntity(Answer answer, QuestionEntity question) {
        return AnswerEntity.builder()
                .id(answer.getId())
                .question(question)
                .text(answer.getText())
                .correct(answer.isCorrect())
                .explanation(answer.getExplanation())
                .build();
    }
}
