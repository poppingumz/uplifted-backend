package fontys.s3.uplifted.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private Long id;
    private Long courseId;
    private Long userId;
    private String reviewText;
    private double rating;
}
