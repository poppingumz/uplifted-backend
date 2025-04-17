package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.ProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<ProgressEntity, Long> {

    List<ProgressEntity> findByUserId(Long userId);
    List<ProgressEntity> findByCourseId(Long courseId);
    Optional<ProgressEntity> findByUserIdAndCourseId(Long userId, Long courseId);
}
