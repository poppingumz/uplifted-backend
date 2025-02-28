package fontys.s3.uplifted.persistence.entity;

import fontys.s3.uplifted.domain.Course;
import fontys.s3.uplifted.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "user_course",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> enrolledCourses;

    public UserEntity(Long id, String username, String email, String password, Role role, Set<Course> enrolledCourses) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enrolledCourses = enrolledCourses;
    }
}
