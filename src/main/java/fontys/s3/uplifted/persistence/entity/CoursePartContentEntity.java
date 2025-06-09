package fontys.s3.uplifted.persistence.entity;

import fontys.s3.uplifted.domain.enums.ContentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_part_content")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoursePartContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ContentType contentType;
    private Long contentId;
    private String title;

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = false)
    private CoursePartEntity part;
}
