package khucnhan.project.homehero.service.impl;

import khucnhan.project.homehero.dto.request.ReviewRequest;
import khucnhan.project.homehero.dto.response.ReviewResponse;
import khucnhan.project.homehero.enums.BookingStatus;
import khucnhan.project.homehero.exception.BadRequestException;
import khucnhan.project.homehero.exception.ResourceNotFoundException;
import khucnhan.project.homehero.model.Booking;
import khucnhan.project.homehero.model.Review;
import khucnhan.project.homehero.model.WorkerProfile;
import khucnhan.project.homehero.repository.BookingRepository;
import khucnhan.project.homehero.repository.ReviewRepository;
import khucnhan.project.homehero.repository.WorkerProfileRepository;
import khucnhan.project.homehero.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final WorkerProfileRepository workerProfileRepository;

    @Override
    @Transactional
    public ReviewResponse create(ReviewRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        if (booking.getStatus() != BookingStatus.DONE) {
            throw new BadRequestException("Can only review completed bookings");
        }

        if (reviewRepository.existsByBookingId(booking.getId())) {
            throw new BadRequestException("Review already submitted for this booking");
        }

        Review review = new Review();
        review.setBooking(booking);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        // Update worker rating average
        WorkerProfile worker = booking.getWorker();
        Double avg = reviewRepository.calculateAverageRatingByWorkerId(worker.getId());
        worker.setRatingAvg(avg != null ? avg : 0.0);
        workerProfileRepository.save(worker);

        return toResponse(saved);
    }

    @Override
    public ReviewResponse getByBookingId(Long bookingId) {
        Review review = reviewRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found for booking: " + bookingId));
        return toResponse(review);
    }

    @Override
    public List<ReviewResponse> getByWorkerId(Long workerId) {
        return reviewRepository.findByWorkerId(workerId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse toResponse(Review r) {
        return ReviewResponse.builder()
                .id(r.getId())
                .bookingId(r.getBooking().getId())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();
    }
}