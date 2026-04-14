package khucnhan.project.homehero.dto.response;

import khucnhan.project.homehero.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class BookingResponse {
    private Long id;
    private UserResponse user;
    private WorkerProfileResponse worker;
    private AddressResponse address;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status;
    private String note;
    private Double totalPrice;
    private LocalDateTime createdAt;
}