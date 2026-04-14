package khucnhan.project.homehero.service.impl;

import khucnhan.project.homehero.dto.request.PaymentRequest;
import khucnhan.project.homehero.dto.response.PaymentResponse;
import khucnhan.project.homehero.enums.PaymentStatus;
import khucnhan.project.homehero.exception.BadRequestException;
import khucnhan.project.homehero.exception.ResourceNotFoundException;
import khucnhan.project.homehero.model.Booking;
import khucnhan.project.homehero.model.Payment;
import khucnhan.project.homehero.repository.BookingRepository;
import khucnhan.project.homehero.repository.PaymentRepository;
import khucnhan.project.homehero.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + request.getBookingId()));

        if (paymentRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new BadRequestException("Payment already exists for this booking");
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalPrice());
        payment.setMethod(request.getMethod());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(java.time.LocalDateTime.now());

        return toResponse(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse getByBookingId(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for booking: " + bookingId));
        return toResponse(payment);
    }

    @Override
    public PaymentResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional
    public PaymentResponse markAsPaid(Long id) {
        Payment payment = findById(id);
        payment.setStatus(PaymentStatus.PAID);
        return toResponse(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public PaymentResponse markAsFailed(Long id) {
        Payment payment = findById(id);
        payment.setStatus(PaymentStatus.FAILED);
        return toResponse(paymentRepository.save(payment));
    }

    private Payment findById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    public PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .bookingId(p.getBooking().getId())
                .amount(p.getAmount())
                .method(p.getMethod())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .build();
    }
}