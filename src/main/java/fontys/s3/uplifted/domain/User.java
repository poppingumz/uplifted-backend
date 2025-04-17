package fontys.s3.uplifted.domain;

import fontys.s3.uplifted.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
}

