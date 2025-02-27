package fontys.s3.uplifted.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Progress {
    private Long id;
    private Long courseId;
    private Long userId;
    private double progressPercentage;
}
