package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.request.WorkerProfileRequest;
import khucnhan.project.homehero.dto.response.WorkerProfileResponse;
import java.util.List;

public interface WorkerProfileService {
    WorkerProfileResponse create(Long userId, WorkerProfileRequest request);
    WorkerProfileResponse update(Long userId, WorkerProfileRequest request);
    WorkerProfileResponse getById(Long id);
    WorkerProfileResponse getByUserId(Long userId);
    List<WorkerProfileResponse> getAll();
    List<WorkerProfileResponse> getVerified();
    List<WorkerProfileResponse> getBySkill(Long skillId);
    List<WorkerProfileResponse> getByPriceRange(Double min, Double max);
    WorkerProfileResponse verify(Long id);
    void delete(Long id);
}