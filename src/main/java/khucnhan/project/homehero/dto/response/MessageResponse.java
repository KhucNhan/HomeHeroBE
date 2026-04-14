package khucnhan.project.homehero.dto.response;

import khucnhan.project.homehero.enums.MessageType;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class MessageResponse {
    private Long id;
    private Long bookingId;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private String content;
    private MessageType type;
    private LocalDateTime createdAt;
}