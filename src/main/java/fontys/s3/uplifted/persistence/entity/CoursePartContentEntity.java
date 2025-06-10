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

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private CoursePartEntity part;
}
