package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.Progress;
import java.util.List;
import java.util.Optional;

public interface ProgressService {
    List<Progress> getAllProgress();
    Optional<Progress> getProgressById(Long id);
    Progress createProgress(Progress progress);
    Optional<Progress> updateProgress(Long id, Progress progress);
    boolean deleteProgress(Long id);
}
