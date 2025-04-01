package fontys.s3.uplifted.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class File {
    private Long id;
    private String name;
    private String type;
    private byte[] data;
}
