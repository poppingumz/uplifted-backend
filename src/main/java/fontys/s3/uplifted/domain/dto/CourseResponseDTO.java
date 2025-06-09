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
public class CourseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private int enrollmentLimit;
    private boolean published;
    private Long instructorId;
    private byte[] imageData;
    private List<CoursePartDTO> parts;
}