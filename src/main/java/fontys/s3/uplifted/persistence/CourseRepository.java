package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long>, JpaSpecificationExecutor<CourseEntity> {

    List<CourseEntity> findByTitleContainingIgnoreCase(String title);
    List<CourseEntity> findByInstructorId(Long instructorId);
}
