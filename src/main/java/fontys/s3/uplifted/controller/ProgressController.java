package fontys.s3.uplifted.controller;

import fontys.s3.uplifted.business.ProgressService;
import fontys.s3.uplifted.domain.Progress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping
    public ResponseEntity<List<Progress>> getAllProgress() {
        try {
            return ResponseEntity.ok(progressService.getAllProgress());
        } catch (Exception e) {
            log.error("Failed to fetch progress list", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Progress> getProgressById(@PathVariable Long id) {
        try {
            return progressService.getProgressById(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("Progress not found with ID: {}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Failed to fetch progress with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Progress> createProgress(@RequestBody Progress progress) {
        try {
            Progress created = progressService.createProgress(progress);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            log.error("Failed to create progress for user ID {} and course ID {}", progress.getUserId(), progress.getCourseId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id) {
        try {
            boolean deleted = progressService.deleteProgress(id);
            if (deleted) {
                log.info("Progress with ID {} deleted successfully", id);
                return ResponseEntity.noContent().build();
            } else {
                log.warn("Progress with ID {} not found for deletion", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Failed to delete progress with ID: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
