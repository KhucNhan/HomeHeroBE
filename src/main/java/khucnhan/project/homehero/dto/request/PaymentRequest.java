package khucnhan.project.homehero.dto.request;

import jakarta.validation.constraints.NotNull;
import khucnhan.project.homehero.enums.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull private Long bookingId;
    @NotNull private PaymentMethod method;
}