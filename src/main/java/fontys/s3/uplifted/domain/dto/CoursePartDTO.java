package fontys.s3.uplifted.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePartDTO {
    private String title;
    private Integer weekNumber;
    private Integer sequence;
    private List<CoursePartContentDTO> contents;
}
