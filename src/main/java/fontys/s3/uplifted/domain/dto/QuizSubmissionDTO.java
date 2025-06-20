package fontys.s3.uplifted.domain.dto;

import lombok.Data;

@Data
public class QuizSubmissionDTO {
    private Long quizId;
    private Long userId;
    private int score;
    private boolean passed;
}
