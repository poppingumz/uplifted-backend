package fontys.s3.uplifted.persistence.entity;

import fontys.s3.uplifted.domain.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private Integer marks;

    private String correctAnswer;

    private boolean requiresReview;

    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private QuizEntity quiz;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerEntity> answers;

}
