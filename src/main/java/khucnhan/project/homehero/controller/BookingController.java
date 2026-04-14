package khucnhan.project.homehero.controller;

import jakarta.validation.Valid;
import khucnhan.project.homehero.dto.request.BookingRequest;
import khucnhan.project.homehero.dto.response.BookingResponse;
import khucnhan.project.homehero.enums.BookingStatus;
import khucnhan.project.homehero.service.BookingService;
import khucnhan.project.homehero.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    private Long getCurrentUserId(UserDetails userDetails) {
        return userService.getByEmail(userDetails.getUsername()).getId();
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BookingResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                  @Valid @RequestBody BookingRequest request) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(bookingService.create(userId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getById(id));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BookingResponse>> getMyBookings(@AuthenticationPrincipal UserDetails userDetails,
                                                               @RequestParam(required = false) BookingStatus status) {
        Long userId = getCurrentUserId(userDetails);
        if (status != null) {
            return ResponseEntity.ok(bookingService.getByUserAndStatus(userId, status));
        }
        return ResponseEntity.ok(bookingService.getByUser(userId));
    }

    @GetMapping("/worker")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<List<BookingResponse>> getWorkerBookings(@AuthenticationPrincipal UserDetails userDetails,
                                                                   @RequestParam(required = false) BookingStatus status) {
        Long userId = getCurrentUserId(userDetails);
        // workerId here uses the WorkerProfile's id linked to userId
        if (status != null) {
            return ResponseEntity.ok(bookingService.getByWorkerAndStatus(userId, status));
        }
        return ResponseEntity.ok(bookingService.getByWorker(userId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('WORKER', 'ADMIN')")
    public ResponseEntity<BookingResponse> updateStatus(@AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable Long id,
                                                        @RequestParam BookingStatus status) {
        Long requesterId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(bookingService.updateStatus(id, status, requesterId));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> cancel(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable Long id) {
        Long userId = getCurrentUserId(userDetails);
        bookingService.cancel(id, userId);
        return ResponseEntity.noContent().build();
    }
}