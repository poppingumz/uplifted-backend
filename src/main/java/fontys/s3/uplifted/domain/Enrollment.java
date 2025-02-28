package fontys.s3.uplifted.domain;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    private Long id;
    private Long userId;
    private Long courseId;
    private LocalDate enrollmentDate;
}
