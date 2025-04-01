package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.ProgressService;
import fontys.s3.uplifted.business.impl.mapper.ProgressMapper;
import fontys.s3.uplifted.domain.Progress;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.ProgressRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.ProgressEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public ProgressServiceImpl(ProgressRepository progressRepository,
                               CourseRepository courseRepository,
                               UserRepository userRepository) {
        this.progressRepository = progressRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<Progress> getAllProgress() {
        try {
            return progressRepository.getAllProgress()
                    .stream()
                    .map(ProgressMapper::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to fetch all progress records", e);
            throw new RuntimeException("Could not retrieve progress data.");
        }
    }

    public Optional<Progress> getProgressById(Long id) {
        try {
            return progressRepository.getProgressById(id).map(ProgressMapper::toDomain);
        } catch (Exception e) {
            log.error("Failed to get progress by ID: {}", id, e);
            throw new RuntimeException("Could not retrieve progress with ID: " + id);
        }
    }

    public Progress createProgress(Progress progress) {
        try {
            CourseEntity course = courseRepository.getCourseById(progress.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + progress.getCourseId()));

            UserEntity user = userRepository.getUserById(progress.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + progress.getUserId()));

            ProgressEntity entity = ProgressMapper.toEntity(progress, course, user);
            ProgressEntity saved = progressRepository.createProgress(entity);
            log.info("Progress created for user {} in course {}", user.getId(), course.getId());
            return ProgressMapper.toDomain(saved);
        } catch (Exception e) {
            log.error("Failed to create progress", e);
            throw new RuntimeException("Could not create progress.");
        }
    }

    public Optional<Progress> updateProgress(Long id, Progress progress) {
        try {
            CourseEntity course = courseRepository.getCourseById(progress.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + progress.getCourseId()));

            UserEntity user = userRepository.getUserById(progress.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + progress.getUserId()));

            ProgressEntity entity = ProgressMapper.toEntity(progress, course, user);
            return progressRepository.updateProgress(id, entity).map(ProgressMapper::toDomain);
        } catch (Exception e) {
            log.error("Failed to update progress for ID: {}", id, e);
            throw new RuntimeException("Could not update progress.");
        }
    }

    public boolean deleteProgress(Long id) {
        try {
            boolean deleted = progressRepository.deleteProgress(id);
            if (deleted) {
                log.info("Progress with ID {} deleted", id);
            } else {
                log.warn("No progress found with ID {} to delete", id);
            }
            return deleted;
        } catch (Exception e) {
            log.error("Failed to delete progress with ID: {}", id, e);
            throw new RuntimeException("Could not delete progress.");
        }
    }
}
