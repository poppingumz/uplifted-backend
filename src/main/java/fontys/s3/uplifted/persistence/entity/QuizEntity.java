package fontys.s3.uplifted.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "quizzes")
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long courseId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer totalMarks;

    @Column(nullable = false)
    private Integer passingMarks;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "quiz_questions_answers", joinColumns = @JoinColumn(name = "quiz_id"))
    @MapKeyColumn(name = "question")
    @Column(name = "answer")
    private Map<String, String> questionsAndAnswers = new HashMap<>();
}
