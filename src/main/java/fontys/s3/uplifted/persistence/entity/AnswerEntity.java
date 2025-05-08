package fontys.s3.uplifted.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private boolean correct;

    private String explanation;

    @ManyToOne
    @JoinColumn(name = "questionId", nullable = false)
    private QuestionEntity question;
}
