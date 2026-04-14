package khucnhan.project.homehero.model;

import jakarta.persistence.*;
import khucnhan.project.homehero.enums.PaymentMethod;
import khucnhan.project.homehero.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
@Entity
@Table(name = "payments")
@Getter @Setter
public class Payment {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method; // CASH, ONLINE

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, PAID, FAILED

    private LocalDateTime createdAt;

}