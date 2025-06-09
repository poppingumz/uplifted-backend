package fontys.s3.uplifted.domain.dto;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Integer enrollmentLimit;
    private boolean published;
    private Long instructorId;
    private List<CoursePartDTO> parts;
}
