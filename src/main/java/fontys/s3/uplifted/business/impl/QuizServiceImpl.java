package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.QuizService;
import fontys.s3.uplifted.business.impl.exception.EntityNotFoundException;
import fontys.s3.uplifted.business.impl.exception.UnauthorizedAccessException;
import fontys.s3.uplifted.business.impl.exception.QuizException;
import fontys.s3.uplifted.business.impl.mapper.AnswerMapper;
import fontys.s3.uplifted.business.impl.mapper.QuestionMapper;
import fontys.s3.uplifted.business.impl.mapper.QuizMapper;
import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.persistence.CourseRepository;
import fontys.s3.uplifted.persistence.QuizRepository;
import fontys.s3.uplifted.persistence.QuizResultRepository;
import fontys.s3.uplifted.persistence.UserRepository;
import fontys.s3.uplifted.persistence.entity.CourseEntity;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
import fontys.s3.uplifted.persistence.entity.QuizResultEntity;
import fontys.s3.uplifted.persistence.entity.UserEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class QuizServiceImpl implements QuizService {
    private static final String UNAUTHORIZED_UPDATE_MESSAGE = "You are not authorized to update this quiz.";
    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    @Autowired
    private QuizResultRepository quizResultRepository;

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
            CourseEntity course = null;
            if (quiz.getCourseId() != null) {
                course = courseRepository.findById(quiz.getCourseId())
                        .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + quiz.getCourseId()));
            }

            UserEntity creator = userRepository.findById(quiz.getCreatedById())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + quiz.getCreatedById()));

            QuizEntity quizEntity = QuizMapper.toEntity(quiz, course, creator);

            if (quiz.getQuestions() != null) {
                quizEntity.setQuestions(
                        quiz.getQuestions().stream()
                                .map(q -> {
                                    var questionEntity = QuestionMapper.toEntity(q, quizEntity);
                                    if (q.getAnswers() != null) {
                                        questionEntity.setAnswers(
                                                q.getAnswers().stream()
                                                        .map(a -> AnswerMapper.toEntity(a, questionEntity))
                                                        .toList()
                                        );
                                    }
                                    return questionEntity;
                                })
                                .toList()
                );
            }

            QuizEntity savedEntity = quizRepository.save(quizEntity);
            log.info("Quiz created successfully with ID: {}", savedEntity.getId());

            return QuizMapper.toDomain(savedEntity);
        } catch (Exception e) {
            log.error("Failed to create quiz", e);
            throw new QuizException("Could not create quiz.", new UnauthorizedAccessException(UNAUTHORIZED_UPDATE_MESSAGE));
        }
    }

    @Override
    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id).map(QuizMapper::toDomain);
    }

    @Override
    public List<Quiz> getQuizzesByCourseId(Long courseId) {
        try {
            return quizRepository.findByCourseId(courseId)
                    .stream()
                    .map(QuizMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to retrieve quizzes for course ID: {}", courseId, e);
            throw new QuizException("Could not load quizzes for course.", new UnauthorizedAccessException(UNAUTHORIZED_UPDATE_MESSAGE));
        }
    }

    @Override
    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        try {
            QuizEntity existing = quizRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Quiz not found with ID: " + id));

            if (!existing.getCreatedBy().getId().equals(updatedQuiz.getCreatedById())) {
                throw new UnauthorizedAccessException(UNAUTHORIZED_UPDATE_MESSAGE);
            }

            CourseEntity course = null;
            if (updatedQuiz.getCourseId() != null) {
                course = courseRepository.findById(updatedQuiz.getCourseId())
                        .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + updatedQuiz.getCourseId()));
            }

            UserEntity creator = userRepository.findById(updatedQuiz.getCreatedById())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + updatedQuiz.getCreatedById()));

            QuizEntity updatedEntity = QuizMapper.toEntity(updatedQuiz, course, creator);
            updatedEntity.setId(id); // Preserve original ID

            QuizEntity saved = quizRepository.save(updatedEntity);
            return QuizMapper.toDomain(saved);
        } catch (Exception e) {
            log.error("Failed to update quiz with ID: {}", id, e);
            throw new QuizException("Could not update quiz.", new UnauthorizedAccessException(UNAUTHORIZED_UPDATE_MESSAGE));
        }
    }

    @Override
    public void deleteQuiz(Long id) {
        try {
            if (!quizRepository.existsById(id)) {
                throw new EntityNotFoundException("Quiz with ID " + id + " not found.");
            }
            quizRepository.deleteById(id);
            log.info("Quiz with ID {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Failed to delete quiz with ID: {}", id, e);
            throw new QuizException("Could not delete quiz.", new UnauthorizedAccessException(UNAUTHORIZED_UPDATE_MESSAGE));
        }
    }

    @Override
    public List<Quiz> getQuizzesByCreatorId(Long userId) {
        return quizRepository.findByCreatedById(userId)
                .stream()
                .map(QuizMapper::toDomain)
                .toList();
    }

    @Transactional
    @Override
    public void saveQuizResult(Long quizId, Long userId, int score, boolean passed) {
        QuizEntity quiz = quizRepository.getReferenceById(quizId);
        UserEntity user = userRepository.getReferenceById(userId);

        log.info("Saving result: quizId={}, userId={}, score={}, passed={}",
                quiz.getId(), user.getId(), score, passed);

        QuizResultEntity result = new QuizResultEntity();
        result.setQuiz(quiz);
        result.setUser(user);
        result.setScore(score);
        result.setPassed(passed);
        result.setSubmittedAt(LocalDateTime.now());

        quizResultRepository.save(result);
    }
}
