package fontys.s3.uplifted.domain;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {
    private Long id;
    private Long courseId;
    private String title;
    private String description;
    private Integer totalMarks;
    private Integer passingMarks;
    private Map<String, String> questionsAndAnswers;
}
