package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.request.ReviewRequest;
import khucnhan.project.homehero.dto.response.ReviewResponse;
import java.util.List;

public interface ReviewService {
    ReviewResponse create(ReviewRequest request);
    ReviewResponse getByBookingId(Long bookingId);
    List<ReviewResponse> getByWorkerId(Long workerId);
}