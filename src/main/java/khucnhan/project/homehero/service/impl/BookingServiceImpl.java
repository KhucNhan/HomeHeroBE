package khucnhan.project.homehero.service.impl;

import khucnhan.project.homehero.dto.request.BookingRequest;
import khucnhan.project.homehero.dto.response.BookingResponse;
import khucnhan.project.homehero.enums.BookingStatus;
import khucnhan.project.homehero.exception.BadRequestException;
import khucnhan.project.homehero.exception.ResourceNotFoundException;
import khucnhan.project.homehero.model.*;
import khucnhan.project.homehero.repository.*;
import khucnhan.project.homehero.service.BookingService;
import khucnhan.project.homehero.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final WorkerProfileRepository workerProfileRepository;
    private final AddressRepository addressRepository;
    private final NotificationService notificationService;
    private final WorkerProfileServiceImpl workerProfileService;
    private final AddressServiceImpl addressService;
    private final UserServiceImpl userService;

    @Override
    @Transactional
    public BookingResponse create(Long userId, BookingRequest request) {
        User user = findUser(userId);
        WorkerProfile worker = workerProfileRepository.findById(request.getWorkerId())
                .orElseThrow(() -> new ResourceNotFoundException("Worker not found with id: " + request.getWorkerId()));
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + request.getAddressId()));

        if (!address.getUser().getId().equals(userId)) {
            throw new BadRequestException("Address does not belong to this user");
        }

        double hours = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes() / 60.0;
        double totalPrice = hours * worker.getPricePerHour();

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setWorker(worker);
        booking.setAddress(address);
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setNote(request.getNote());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(totalPrice);

        Booking saved = bookingRepository.save(booking);

        notificationService.create(worker.getUser().getId(),
                "You have a new booking request from " + user.getName());

        return toResponse(saved);
    }

    @Override
    public BookingResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public List<BookingResponse> getByUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getByWorker(Long workerId) {
        return bookingRepository.findByWorkerId(workerId).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getByUserAndStatus(Long userId, BookingStatus status) {
        return bookingRepository.findByUserIdAndStatus(userId, status).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> getByWorkerAndStatus(Long workerId, BookingStatus status) {
        return bookingRepository.findByWorkerIdAndStatus(workerId, status).stream()
                .map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse updateStatus(Long id, BookingStatus status, Long requesterId) {
        Booking booking = findById(id);
        booking.setStatus(status);

        if (status == BookingStatus.DONE) {
            WorkerProfile worker = booking.getWorker();
            worker.setTotalJobs(worker.getTotalJobs() + 1);
            workerProfileRepository.save(worker);

            notificationService.create(booking.getUser().getId(),
                    "Your booking has been completed!");
        }

        if (status == BookingStatus.ACCEPTED) {
            notificationService.create(booking.getUser().getId(),
                    "Your booking has been accepted by the worker.");
        }

        if (status == BookingStatus.REJECTED) {
            notificationService.create(booking.getUser().getId(),
                    "Your booking was rejected by the worker.");
        }

        return toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public void cancel(Long id, Long userId) {
        Booking booking = findById(id);
        if (!booking.getUser().getId().equals(userId)) {
            throw new BadRequestException("You are not allowed to cancel this booking");
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("Only PENDING bookings can be cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        notificationService.create(booking.getWorker().getUser().getId(),
                "A booking has been cancelled by the user.");
    }

    private Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public BookingResponse toResponse(Booking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .user(userService.toResponse(b.getUser()))
                .worker(workerProfileService.toResponse(b.getWorker()))
                .address(addressService.toResponse(b.getAddress()))
                .startTime(b.getStartTime())
                .endTime(b.getEndTime())
                .status(b.getStatus())
                .note(b.getNote())
                .totalPrice(b.getTotalPrice())
                .createdAt(b.getCreatedAt())
                .build();
    }
}