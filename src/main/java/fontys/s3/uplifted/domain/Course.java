package fontys.s3.uplifted.domain;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    private Long id;
    private Long teacherId;
    private String title;
    private String description;
    private Set<Long> enrolledStudentIds;
}
