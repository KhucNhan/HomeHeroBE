package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.request.BookingRequest;
import khucnhan.project.homehero.dto.response.BookingResponse;
import khucnhan.project.homehero.enums.BookingStatus;
import java.util.List;

public interface BookingService {
    BookingResponse create(Long userId, BookingRequest request);
    BookingResponse getById(Long id);
    List<BookingResponse> getByUser(Long userId);
    List<BookingResponse> getByWorker(Long workerId);
    List<BookingResponse> getByUserAndStatus(Long userId, BookingStatus status);
    List<BookingResponse> getByWorkerAndStatus(Long workerId, BookingStatus status);
    BookingResponse updateStatus(Long id, BookingStatus status, Long requesterId);
    void cancel(Long id, Long userId);
}