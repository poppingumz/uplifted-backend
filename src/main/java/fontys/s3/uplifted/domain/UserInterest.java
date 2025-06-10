package fontys.s3.uplifted.domain;

import fontys.s3.uplifted.domain.enums.InterestCategory;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInterest {
    private Long id;
    private Long userId;
    private InterestCategory category;
}