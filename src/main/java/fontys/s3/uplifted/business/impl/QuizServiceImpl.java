package fontys.s3.uplifted.business.impl;

import fontys.s3.uplifted.business.QuizService;
import fontys.s3.uplifted.business.impl.mapper.QuizMapper;
import fontys.s3.uplifted.domain.Quiz;
import fontys.s3.uplifted.persistence.QuizRepository;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    public QuizServiceImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz createQuiz(Quiz quiz) {
        try {
            QuizEntity entity = QuizMapper.toEntity(quiz);
            QuizEntity savedEntity = quizRepository.save(entity);
            log.info("Quiz created successfully with ID: {}", savedEntity.getId());
            return QuizMapper.toDomain(savedEntity);
        } catch (Exception e) {
            log.error("Failed to create quiz", e);
            throw new RuntimeException("Could not create quiz. Please try again.");
        }
    }

    public Optional<Quiz> getQuizById(Long id) {
        try {
            return quizRepository.findById(id).map(QuizMapper::toDomain);
        } catch (Exception e) {
            log.error("Error retrieving quiz with ID: {}", id, e);
            throw new RuntimeException("Quiz not found with ID: " + id);
        }
    }

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

    public Quiz updateQuiz(Long id, Quiz updatedQuiz) {
        try {
            QuizEntity entity = QuizMapper.toEntity(updatedQuiz);
            return quizRepository.update(id, entity)
                    .map(QuizMapper::toDomain)
                    .orElseThrow(() -> new RuntimeException("Quiz not found for update with ID: " + id));
        } catch (Exception e) {
            log.error("Failed to update quiz with ID: {}", id, e);
            throw new RuntimeException("Could not update quiz.");
        }
    }

    public void deleteQuiz(Long id) {
        try {
            quizRepository.delete(id);
            log.info("Quiz with ID {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Failed to delete quiz with ID: {}", id, e);
            throw new RuntimeException("Could not delete quiz.");
        }
    }
}
