package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByCourseId(Long courseId);
    List<ReviewEntity> findByUserId(Long userId);
    List<ReviewEntity> findByRatingGreaterThanEqual(int rating);
    List<ReviewEntity> findByRatingLessThanEqual(int rating);
}
