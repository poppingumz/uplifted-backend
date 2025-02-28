package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.ProgressService;
import fontys.s3.uplifted.domain.Progress;
import fontys.s3.uplifted.exception.NotFoundException;
import fontys.s3.uplifted.persistence.ProgressRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.ProgressEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public ProgressServiceImpl(ProgressRepository progressRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.progressRepository = progressRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Progress> getAllProgress() {
        return progressRepository.getAllProgress()
                .stream()
                .map(ProgressMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Progress> getProgressById(Long id) {
        return progressRepository.getProgressById(id).map(ProgressMapper::toDomain);
    }

    @Transactional
    @Override
    public Progress createProgress(Progress progress) {
        CourseEntity course = courseRepository.getCourseById(progress.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found with ID: " + progress.getCourseId()));

        UserEntity user = userRepository.getUserById(progress.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + progress.getUserId()));

        double progressPercentage = (double) progress.getCompletedLessons() / progress.getTotalLessons() * 100;
        progress.setProgressPercentage(progressPercentage);

        ProgressEntity entity = ProgressMapper.toEntity(progress, course, user);
        ProgressEntity savedEntity = progressRepository.createProgress(entity);

        return ProgressMapper.toDomain(savedEntity);
    }

    @Transactional
    @Override
    public Optional<Progress> updateProgress(Long id, Progress progress) {
        CourseEntity course = courseRepository.getCourseById(progress.getCourseId())
                .orElseThrow(() -> new NotFoundException("Course not found"));

        UserEntity user = userRepository.getUserById(progress.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        double progressPercentage = (double) progress.getCompletedLessons() / progress.getTotalLessons() * 100;
        progress.setProgressPercentage(progressPercentage);

        ProgressEntity entity = ProgressMapper.toEntity(progress, course, user);
        return progressRepository.updateProgress(id, entity).map(ProgressMapper::toDomain);
    }

    @Transactional
    @Override
    public boolean deleteProgress(Long id) {
        return progressRepository.deleteProgress(id);
    }
}
