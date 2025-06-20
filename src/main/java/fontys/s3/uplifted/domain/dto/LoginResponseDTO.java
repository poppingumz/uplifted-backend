package fontys.s3.uplifted.domain.dto;

import fontys.s3.uplifted.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private Long id;
    private String email;
    private String username;
    private Role role;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private byte[] profileImage;
    private String bio;
    private LocalDate joinedDate;
    private boolean active;
    private String token;
}
