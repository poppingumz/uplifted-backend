package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.EnrollmentEntity;
import fontys.s3.uplifted.persistence.entity.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<EnrollmentEntity, EnrollmentId> {
    List<EnrollmentEntity> findByUserId(Long userId);
    List<EnrollmentEntity> findByCourseId(Long courseId);
}
