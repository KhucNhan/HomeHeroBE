package khucnhan.project.homehero.controller;

import jakarta.validation.Valid;
import khucnhan.project.homehero.dto.request.MessageRequest;
import khucnhan.project.homehero.dto.response.MessageResponse;
import khucnhan.project.homehero.service.MessageService;
import khucnhan.project.homehero.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    private Long getCurrentUserId(UserDetails userDetails) {
        return userService.getByEmail(userDetails.getUsername()).getId();
    }

    @PostMapping
    public ResponseEntity<MessageResponse> send(@AuthenticationPrincipal UserDetails userDetails,
                                                @Valid @RequestBody MessageRequest request) {
        Long senderId = getCurrentUserId(userDetails);
        MessageResponse response = messageService.send(senderId, request);

        // Push via WebSocket to receiver
        messagingTemplate.convertAndSendToUser(
                request.getReceiverId().toString(),
                "/queue/messages",
                response
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<MessageResponse>> getByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(messageService.getByBooking(bookingId));
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<MessageResponse>> getMyConversation(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(messageService.getConversation(userId));
    }

    // WebSocket endpoint
    @MessageMapping("/chat.send")
    public void handleWebSocketMessage(@Payload MessageRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        Long senderId = getCurrentUserId(userDetails);
        MessageResponse response = messageService.send(senderId, request);

        messagingTemplate.convertAndSendToUser(
                request.getReceiverId().toString(),
                "/queue/messages",
                response
        );
    }
}