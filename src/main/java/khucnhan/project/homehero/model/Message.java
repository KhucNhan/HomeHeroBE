package khucnhan.project.homehero.model;

import jakarta.persistence.*;
import khucnhan.project.homehero.enums.MessageType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "messages")
@Getter @Setter
public class Message extends BaseEntity{

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type; // TEXT, IMAGE

}