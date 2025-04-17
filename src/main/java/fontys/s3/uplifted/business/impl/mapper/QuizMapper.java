package fontys.s3.uplifted.business.impl.mapper;

import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;

import java.util.stream.Collectors;

public final class QuizMapper {

    public static Quiz toDomain(QuizEntity entity) {
        return Quiz.builder()
                .id(entity.getId())
                .courseId(entity.getCourse().getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .totalMarks(entity.getTotalMarks())
                .passingMarks(entity.getPassingMarks())
                .createdById(entity.getCreatedBy().getId())
                .questions(entity.getQuestions().stream()
                        .map(QuestionMapper::toDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public static QuizEntity toEntity(Quiz quiz, CourseEntity course, UserEntity creator) {
        return QuizEntity.builder()
                .id(quiz.getId())
                .course(course)
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .totalMarks(quiz.getTotalMarks())
                .passingMarks(quiz.getPassingMarks())
                .createdBy(creator)
                .build();
    }

}

