package fontys.s3.uplifted.persistence.impl;

import fontys.s3.uplifted.persistence.QuizRepository;
import fontys.s3.uplifted.persistence.entity.QuizEntity;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FakeQuizRepositoryImpl implements QuizRepository {
    private final Map<Long, QuizEntity> quizzes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public QuizEntity save(QuizEntity quiz) {
        if (quiz.getId() == null) {
            quiz.setId(idGenerator.getAndIncrement());
        }
        quizzes.put(quiz.getId(), quiz);
        return quiz;
    }

    public Optional<QuizEntity> findById(Long id) {
        return Optional.ofNullable(quizzes.get(id));
    }

    public List<QuizEntity> findByCourseId(Long courseId) {
        List<QuizEntity> result = new ArrayList<>();
        for (QuizEntity quiz : quizzes.values()) {
            if (quiz.getCourseId().equals(courseId)) {
                result.add(quiz);
            }
        }
        return result;
    }

    public Optional<QuizEntity> update(Long id, QuizEntity quiz) {
        if (quizzes.containsKey(id)) {
            quiz.setId(id);
            quizzes.put(id, quiz);
            return Optional.of(quiz);
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        quizzes.remove(id);
    }
}
