package fontys.sem3.school.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCountryRequest {
    @NotBlank(message = "Country code is required")
    private String code;

    @NotBlank(message = "Country name is required")
    private String name;
}
