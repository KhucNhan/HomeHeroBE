package khucnhan.project.homehero.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Table(name = "worker_profiles")
@Getter @Setter
public class WorkerProfile extends BaseEntity{

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String description;

    private Double ratingAvg = 0.0;

    private Integer totalJobs = 0;

    private Boolean isVerified = false;

    private Double pricePerHour;

    private Integer workingRadiusKm;

    @ManyToMany
    @JoinTable(
            name = "worker_skills",
            joinColumns = @JoinColumn(name = "worker_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills;

}