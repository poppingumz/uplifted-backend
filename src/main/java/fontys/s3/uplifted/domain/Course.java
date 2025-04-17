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
    private Long instructorId;
    private Set<Long> enrolledStudentIds;
    private int enrollmentLimit;
    private boolean published;
    private String category;
}
