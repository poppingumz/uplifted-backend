package fontys.s3.uplifted.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePart {
    private String title;
    private Integer weekNumber;
    private Integer sequence;
    private List<CoursePartContent> contents;
}