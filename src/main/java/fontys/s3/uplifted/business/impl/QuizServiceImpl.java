package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.QuizService;
import fontys.s3.uplifted.business.impl.mapper.QuizMapper;
import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.QuizRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public QuizServiceImpl(QuizRepository quizRepository,
                           CourseRepository courseRepository,
                           UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Quiz createQuiz(Quiz quiz) {
        try {
            CourseEntity course = courseRepository.findById(quiz.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + quiz.getCourseId()));

            UserEntity creator = userRepository.findById(quiz.getCreatedById())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + quiz.getCreatedById()));

            QuizEntity entity = QuizMapper.toEntity(quiz, course, creator);
            QuizEntity savedEntity = quizRepository.save(entity);

            log.info("Quiz created successfully with ID: {}", savedEntity.getId());
            return QuizMapper.toDomain(savedEntity);
        } catch (Exception e) {
            log.error("Failed to create quiz", e);
            throw new RuntimeException("Could not create quiz. Please try again.");
        }
    }

    @Override
    public Optional<Quiz> getQuizById(Long id) {
        try {
            return quizRepository.findById(id)
                    .map(QuizMapper::toDomain);
        } catch (Exception e) {
            log.error("Error retrieving quiz with ID: {}", id, e);
            throw new RuntimeException("Quiz not found with ID: " + id);
        }
    }

    @Override
    public List<Quiz> getQuizzesByCourseId(Long courseId) {
        try {
            return quizRepository.findByCourseId(courseId)
                    .stream()
                    .map(QuizMapper::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Failed to retrieve quizzes for course ID: {}", courseId, e);
            throw new RuntimeException("Could not load quizzes for course.");
        }
    }

    @Override
    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        try {
            return quizRepository.findById(id)
                    .map(existing -> {
                        CourseEntity course = courseRepository.findById(updatedQuiz.getCourseId())
                                .orElseThrow(() -> new RuntimeException("Course not found"));

                        UserEntity creator = userRepository.findById(updatedQuiz.getCreatedById())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                        QuizEntity updatedEntity = QuizMapper.toEntity(updatedQuiz, course, creator);
                        updatedEntity.setId(id);

                        QuizEntity saved = quizRepository.save(updatedEntity);
                        return QuizMapper.toDomain(saved);
                    })
                    .orElseThrow(() -> new RuntimeException("Quiz not found with ID: " + id));
        } catch (Exception e) {
            log.error("Failed to update quiz with ID: {}", id, e);
            throw new RuntimeException("Could not update quiz.");
        }
    }

    @Override
    public void deleteQuiz(Long id) {
        try {
            if (!quizRepository.existsById(id)) {
                throw new RuntimeException("Quiz with ID " + id + " not found.");
            }

            quizRepository.deleteById(id);
            log.info("Quiz with ID {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Failed to delete quiz with ID: {}", id, e);
            throw new RuntimeException("Could not delete quiz.");
        }
    }
}
