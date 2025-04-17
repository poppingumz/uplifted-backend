package fontys.s3.uplifted.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {
    private Long id;
    private String text;
    private boolean correct;
    private String explanation;
}

