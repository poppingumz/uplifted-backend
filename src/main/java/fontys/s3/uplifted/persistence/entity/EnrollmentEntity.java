package fontys.s3.uplifted.persistence.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentEntity {
    private Long id;
    private Long userId;
    private Long courseId;
    private LocalDate enrollmentDate;
}
