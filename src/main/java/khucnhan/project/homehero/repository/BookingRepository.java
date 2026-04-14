package khucnhan.project.homehero.repository;

import khucnhan.project.homehero.enums.BookingStatus;
import khucnhan.project.homehero.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByWorkerId(Long workerId);
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
    List<Booking> findByWorkerIdAndStatus(Long workerId, BookingStatus status);
    boolean existsByUserIdAndWorkerId(Long userId, Long workerId);
}