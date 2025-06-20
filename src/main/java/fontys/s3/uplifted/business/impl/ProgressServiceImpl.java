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

@Service
@Slf4j
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
        return progressRepository.findAll()
                .stream()
                .map(ProgressMapper::toDomain)
                .toList();
    }

    public Optional<Progress> getProgressById(Long id) {
        return progressRepository.findById(id)
                .map(ProgressMapper::toDomain);
    }

    public Progress createProgress(Progress progress) {
        CourseEntity course = courseRepository.findById(progress.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        UserEntity user = userRepository.findById(progress.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProgressEntity entity = ProgressMapper.toEntity(progress, course, user);
        return ProgressMapper.toDomain(progressRepository.save(entity));
    }

    public Optional<Progress> updateProgress(Long id, Progress progress) {
        CourseEntity course = courseRepository.findById(progress.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        UserEntity user = userRepository.findById(progress.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return progressRepository.findById(id).map(existing -> {
            ProgressEntity entity = ProgressMapper.toEntity(progress, course, user);
            entity.setId(id);
            return ProgressMapper.toDomain(progressRepository.save(entity));
        });
    }

    public boolean deleteProgress(Long id) {
        if (!progressRepository.existsById(id)) {
            return false;
        }
        progressRepository.deleteById(id);
        return true;
    }
}

