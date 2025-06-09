package fontys.s3.uplifted.domain;

import fontys.s3.uplifted.domain.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePartContent {
    private String title;
    private ContentType contentType;
    private Long contentId;
}
