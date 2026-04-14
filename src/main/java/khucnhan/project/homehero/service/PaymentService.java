package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.request.PaymentRequest;
import khucnhan.project.homehero.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse create(PaymentRequest request);
    PaymentResponse getByBookingId(Long bookingId);
    PaymentResponse getById(Long id);
    PaymentResponse markAsPaid(Long id);
    PaymentResponse markAsFailed(Long id);
}