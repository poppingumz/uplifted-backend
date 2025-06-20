package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.QuizResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResultEntity, Long> {
    List<QuizResultEntity> findByUserIdAndQuizId(Long userId, Long quizId);
}
