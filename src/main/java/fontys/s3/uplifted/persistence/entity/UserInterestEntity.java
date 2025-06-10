package fontys.s3.uplifted.persistence.entity;

import fontys.s3.uplifted.domain.enums.InterestCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_interest")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInterestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterestCategory category;
}