package fontys.s3.uplifted.persistence.entity;

import fontys.s3.uplifted.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "\"user\"")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Lob
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
