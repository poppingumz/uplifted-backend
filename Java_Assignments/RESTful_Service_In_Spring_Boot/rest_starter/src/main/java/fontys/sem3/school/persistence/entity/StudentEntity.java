package fontys.sem3.school.persistence.entity;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class StudentEntity {
    private Long id;
    private Long pcn;
    private String name;
    private CountryEntity country;
}
