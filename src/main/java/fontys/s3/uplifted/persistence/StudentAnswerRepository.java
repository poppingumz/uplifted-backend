package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.StudentAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAnswerRepository extends JpaRepository<StudentAnswerEntity, Long> {
    List<StudentAnswerEntity> findByReviewedFalse();
    List<StudentAnswerEntity> findByUserId(Long userId);
    List<StudentAnswerEntity> findByQuizIdAndUserId(Long quizId, Long userId);
}
