package khucnhan.project.homehero.service.impl;

import khucnhan.project.homehero.dto.request.MessageRequest;
import khucnhan.project.homehero.dto.response.MessageResponse;
import khucnhan.project.homehero.exception.ResourceNotFoundException;
import khucnhan.project.homehero.model.Booking;
import khucnhan.project.homehero.model.Message;
import khucnhan.project.homehero.model.User;
import khucnhan.project.homehero.repository.BookingRepository;
import khucnhan.project.homehero.repository.MessageRepository;
import khucnhan.project.homehero.repository.UserRepository;
import khucnhan.project.homehero.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MessageResponse send(Long senderId, MessageRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + senderId));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getReceiverId()));

        Message message = new Message();
        message.setBooking(booking);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setType(request.getType());

        return toResponse(messageRepository.save(message));
    }

    @Override
    public List<MessageResponse> getByBooking(Long bookingId) {
        return messageRepository.findByBookingIdOrderByCreatedAtAsc(bookingId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> getConversation(Long userId) {
        return messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse toResponse(Message m) {
        return MessageResponse.builder()
                .id(m.getId())
                .bookingId(m.getBooking().getId())
                .senderId(m.getSender().getId())
                .senderName(m.getSender().getName())
                .receiverId(m.getReceiver().getId())
                .receiverName(m.getReceiver().getName())
                .content(m.getContent())
                .type(m.getType())
                .createdAt(m.getCreatedAt())
                .build();
    }
}