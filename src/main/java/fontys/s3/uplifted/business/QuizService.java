package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.Quiz;

import java.util.List;
import java.util.Optional;

public interface QuizService {
    Quiz createQuiz(Quiz quiz);
    Optional<Quiz> getQuizById(Long id);
    List<Quiz> getQuizzesByCourseId(Long courseId);
    Quiz updateQuiz(Long id, Quiz updatedQuiz);
    void deleteQuiz(Long id);
}
