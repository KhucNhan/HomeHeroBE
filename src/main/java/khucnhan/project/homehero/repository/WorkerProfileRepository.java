package khucnhan.project.homehero.repository;

import khucnhan.project.homehero.model.WorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerProfileRepository extends JpaRepository<WorkerProfile, Long> {
    Optional<WorkerProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    List<WorkerProfile> findByIsVerifiedTrue();

    @Query("SELECT wp FROM WorkerProfile wp JOIN wp.skills s WHERE s.id = :skillId AND wp.isVerified = true")
    List<WorkerProfile> findBySkillIdAndVerified(@Param("skillId") Long skillId);

    @Query("SELECT wp FROM WorkerProfile wp WHERE wp.pricePerHour BETWEEN :min AND :max AND wp.isVerified = true")
    List<WorkerProfile> findByPriceRange(@Param("min") Double min, @Param("max") Double max);
}