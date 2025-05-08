package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    List<CourseEntity> findByTitleContainingIgnoreCase(String title);
    List<CourseEntity> findByInstructorId(Long instructorId);
    boolean existsByTitle(String title);
}
