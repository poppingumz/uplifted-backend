package fontys.s3.uplifted.domain;

import lombok.*;

import java.util.List;

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

    private Long createdById;
    private List<Question> questions;
}
