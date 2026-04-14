package khucnhan.project.homehero.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class NotificationResponse {
    private Long id;
    private Long userId;
    private String content;
    private Boolean isRead;
    private LocalDateTime createdAt;
}