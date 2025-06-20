package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.QuizResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResultEntity, Long> {
    List<QuizResultEntity> findByUserIdAndQuizId(Long userId, Long quizId);
    @Query("SELECT qr.quiz.id FROM QuizResultEntity qr WHERE qr.user.id = :userId AND qr.passed = true")
    List<Long> findPassedQuizIdsByUserId(@Param("userId") Long userId);

}
