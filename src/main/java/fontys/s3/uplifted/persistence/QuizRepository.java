package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.QuizEntity;

import java.util.List;
import java.util.Optional;

public interface QuizRepository {
    QuizEntity save(QuizEntity quiz);
    Optional<QuizEntity> findById(Long id);
    List<QuizEntity> findByCourseId(Long courseId);
    Optional<QuizEntity> update(Long id, QuizEntity quiz);
    void delete(Long id);
}
