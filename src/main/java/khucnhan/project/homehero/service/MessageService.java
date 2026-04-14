package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.request.MessageRequest;
import khucnhan.project.homehero.dto.response.MessageResponse;
import java.util.List;

public interface MessageService {
    MessageResponse send(Long senderId, MessageRequest request);
    List<MessageResponse> getByBooking(Long bookingId);
    List<MessageResponse> getConversation(Long userId);
}