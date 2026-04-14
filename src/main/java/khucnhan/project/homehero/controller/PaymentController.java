package khucnhan.project.homehero.controller;

import jakarta.validation.Valid;
import khucnhan.project.homehero.dto.request.PaymentRequest;
import khucnhan.project.homehero.dto.response.PaymentResponse;
import khucnhan.project.homehero.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getByBookingId(bookingId));
    }

    @PatchMapping("/{id}/paid")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> markAsPaid(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.markAsPaid(id));
    }

    @PatchMapping("/{id}/failed")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> markAsFailed(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.markAsFailed(id));
    }
}