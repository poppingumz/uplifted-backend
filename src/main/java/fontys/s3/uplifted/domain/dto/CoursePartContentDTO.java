package fontys.s3.uplifted.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePartContentDTO {
    private String title;
    private String contentType;
    private Long contentId;
}
