package fontys.s3.uplifted.domain;

import lombok.*;

import java.util.List;
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
    private byte[] imageData;
    private Long instructorId;
    private String category;
    private Set<Long> enrolledStudentIds;
    private int enrollmentLimit;
    private boolean published;
    private double rating;
    private int numberOfReviews;
    private List<CoursePart> parts;
}
