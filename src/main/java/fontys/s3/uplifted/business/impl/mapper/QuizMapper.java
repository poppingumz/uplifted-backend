package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.persistence.entity.*;

import java.util.List;
import java.util.stream.Collectors;

public final class QuizMapper {

    private QuizMapper() {
    }

    public static Quiz toDomain(QuizEntity entity) {
        return Quiz.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .totalMarks(entity.getTotalMarks())
                .passingMarks(entity.getPassingMarks())
                .courseId(entity.getCourse() != null ? entity.getCourse().getId() : null)
                .createdById(entity.getCreatedBy().getId())
                .questions(entity.getQuestions() != null
                        ? entity.getQuestions().stream().map(QuestionMapper::toDomain).toList()
                        : List.of())
                .build();
    }



    public static QuizEntity toEntity(Quiz quiz, CourseEntity course, UserEntity creator) {
        QuizEntity quizEntity = QuizEntity.builder()
                .id(quiz.getId())
                .course(course)
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .totalMarks(quiz.getTotalMarks())
                .passingMarks(quiz.getPassingMarks())
                .createdBy(creator)
                .build();

        if (quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
            var questionEntities = quiz.getQuestions().stream()
                    .map(q -> {
                        QuestionEntity questionEntity = QuestionMapper.toEntity(q, quizEntity);

                        if (q.getAnswers() != null && !q.getAnswers().isEmpty()) {
                            var answerEntities = q.getAnswers().stream()
                                    .map(a -> AnswerMapper.toEntity(a, questionEntity))
                                    .collect(Collectors.toList());
                            questionEntity.setAnswers(answerEntities);
                        }

                        return questionEntity;
                    })
                    .collect(Collectors.toList());

            quizEntity.setQuestions(questionEntities);
        }

        return quizEntity;
    }
}
