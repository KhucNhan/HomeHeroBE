package khucnhan.project.homehero.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import khucnhan.project.homehero.enums.MessageType;
import lombok.Data;

@Data
public class MessageRequest {
    @NotNull private Long bookingId;
    @NotNull private Long receiverId;
    @NotBlank private String content;
    private MessageType type = MessageType.TEXT;
}