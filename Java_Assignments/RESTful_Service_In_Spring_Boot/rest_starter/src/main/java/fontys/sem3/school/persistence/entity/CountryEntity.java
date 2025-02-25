package fontys.sem3.school.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
public class CountryEntity {
    private Long id;

    private String name;

    private String code;
}
