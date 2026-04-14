package khucnhan.project.homehero.dto.response;

import khucnhan.project.homehero.enums.PaymentMethod;
import khucnhan.project.homehero.enums.PaymentStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class PaymentResponse {
    private Long id;
    private Long bookingId;
    private Double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}