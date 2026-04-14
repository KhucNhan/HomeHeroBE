package khucnhan.project.homehero.controller;

import jakarta.validation.Valid;
import khucnhan.project.homehero.dto.request.ReviewRequest;
import khucnhan.project.homehero.dto.response.ReviewResponse;
import khucnhan.project.homehero.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.create(request));
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ReviewResponse> getByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(reviewService.getByBookingId(bookingId));
    }

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<List<ReviewResponse>> getByWorker(@PathVariable Long workerId) {
        return ResponseEntity.ok(reviewService.getByWorkerId(workerId));
    }
}