package fontys.s3.uplifted.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "courses")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long teacherId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ElementCollection
    private Set<Long> enrolledStudentIds;
}
