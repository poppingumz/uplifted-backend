package fontys.s3.uplifted.business;

import fontys.s3.uplifted.domain.StudentAnswer;

import java.util.List;

public interface StudentAnswerService {
    void submitAnswer(StudentAnswer studentAnswer);
    void reviewAnswer(Long answerId, int marks, String feedback);
    List<StudentAnswer> getPendingReviews();
    List<StudentAnswer> getAnswersByUser(Long userId);
}
