package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.response.NotificationResponse;
import java.util.List;

public interface NotificationService {
    NotificationResponse create(Long userId, String content);
    List<NotificationResponse> getByUser(Long userId);
    List<NotificationResponse> getUnread(Long userId);
    long countUnread(Long userId);
    void markAsRead(Long id);
    void markAllAsRead(Long userId);
}