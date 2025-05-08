package fontys.s3.uplifted.domain;

import fontys.s3.uplifted.domain.enums.Role;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    private byte[] profileImage;
    private String bio;
    private LocalDate joinedDate;
    private boolean isActive;
}


