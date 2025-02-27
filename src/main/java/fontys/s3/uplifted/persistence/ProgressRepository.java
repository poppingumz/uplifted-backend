package fontys.s3.uplifted.persistence;

import fontys.s3.uplifted.persistence.entity.ProgressEntity;
import java.util.List;
import java.util.Optional;

public interface ProgressRepository {
    List<ProgressEntity> getAllProgress();
    Optional<ProgressEntity> getProgressById(Long id);
    ProgressEntity createProgress(ProgressEntity progress);
    Optional<ProgressEntity> updateProgress(Long id, ProgressEntity progress);
    boolean deleteProgress(Long id);
}
