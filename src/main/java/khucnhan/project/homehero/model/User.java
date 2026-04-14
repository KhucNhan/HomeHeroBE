package khucnhan.project.homehero.model;

import jakarta.persistence.*;
import khucnhan.project.homehero.enums.Role;
import khucnhan.project.homehero.enums.Status;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity{

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role; // USER, WORKER, ADMIN

    @Enumerated(EnumType.STRING)
    private Status status; // ACTIVE, BANNED

    @OneToMany(mappedBy = "user")
    private List<Address> addresses;
}
