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
    private String title;
    private String description;
    private String instructorName;
    private Set<Long> enrolledStudentIds;
}
