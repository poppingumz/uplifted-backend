package fontys.s3.uplifted.persistence.impl;

import fontys.s3.uplifted.persistence.ProgressRepository;
import fontys.s3.uplifted.persistence.entity.ProgressEntity;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeProgressRepositoryImpl implements ProgressRepository {
    private final Map<Long, ProgressEntity> progressRecords = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<ProgressEntity> getAllProgress() {
        return new ArrayList<>(progressRecords.values());
    }

    public Optional<ProgressEntity> getProgressById(Long id) {
        return Optional.ofNullable(progressRecords.get(id));
    }

    public ProgressEntity createProgress(ProgressEntity progress) {
        long newId = idGenerator.getAndIncrement();
        progress.setId(newId);
        progressRecords.put(newId, progress);
        return progress;
    }

    public Optional<ProgressEntity> updateProgress(Long id, ProgressEntity updatedProgress) {
        if (!progressRecords.containsKey(id)) {
            return Optional.empty();
        }
        updatedProgress.setId(id);
        progressRecords.put(id, updatedProgress);
        return Optional.of(updatedProgress);
    }

    public boolean deleteProgress(Long id) {
        return progressRecords.remove(id) != null;
    }
}
