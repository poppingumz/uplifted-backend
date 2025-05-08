package fontys.s3.uplifted.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long quizId;

    @Column(nullable = false)
    private Long questionId;

    @Column(columnDefinition = "TEXT")
    private String submittedAnswer;

    private Integer awardedMarks;

    private String mentorFeedback;

    @Column(nullable = false)
    private boolean reviewed;
}
