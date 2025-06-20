package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Long> {

    List<QuizEntity> findByCourseId(Long courseId);
    List<QuizEntity> findByTitleContainingIgnoreCase(String keyword);
    List<QuizEntity> findByCreatedById(Long userId);
}
