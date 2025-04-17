package fontys.s3.uplifted.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswer {
    private Long id;
    private Long userId;
    private Long quizId;
    private Long questionId;

    private String submittedAnswer;
    private Integer awardedMarks;
    private String mentorFeedback;

    private boolean reviewed;
}
