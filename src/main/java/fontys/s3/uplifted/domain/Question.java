package fontys.s3.uplifted.domain;

import fontys.s3.uplifted.domain.enums.QuestionType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    private Long id;
    private String text;
    private QuestionType type;
    private Integer marks;

    private List<Answer> answers;

    private String correctAnswer;

    private boolean requiresReview;
}

