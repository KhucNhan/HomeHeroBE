package khucnhan.project.homehero.service.impl;

import khucnhan.project.homehero.dto.request.WorkerProfileRequest;
import khucnhan.project.homehero.dto.response.SkillResponse;
import khucnhan.project.homehero.dto.response.WorkerProfileResponse;
import khucnhan.project.homehero.enums.Role;
import khucnhan.project.homehero.exception.BadRequestException;
import khucnhan.project.homehero.exception.ResourceNotFoundException;
import khucnhan.project.homehero.model.Skill;
import khucnhan.project.homehero.model.User;
import khucnhan.project.homehero.model.WorkerProfile;
import khucnhan.project.homehero.repository.SkillRepository;
import khucnhan.project.homehero.repository.UserRepository;
import khucnhan.project.homehero.repository.WorkerProfileRepository;
import khucnhan.project.homehero.service.WorkerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerProfileServiceImpl implements WorkerProfileService {

    private final WorkerProfileRepository workerProfileRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Override
    @Transactional
    public WorkerProfileResponse create(Long userId, WorkerProfileRequest request) {
        if (workerProfileRepository.existsByUserId(userId))
            throw new BadRequestException("Worker profile already exists for this user");

        User user = findUser(userId);
        WorkerProfile profile = new WorkerProfile();
        profile.setUser(user);
        profile.setDescription(request.getDescription());
        profile.setPricePerHour(request.getPricePerHour());
        profile.setWorkingRadiusKm(request.getWorkingRadiusKm());
        profile.setSkills(resolveSkills(request.getSkillIds()));
        profile.setIsVerified(false);
        profile.setRatingAvg(0.0);
        profile.setTotalJobs(0);
        user.setRole(Role.WORKER);

        return toResponse(workerProfileRepository.save(profile));
    }

    @Override
    @Transactional
    public WorkerProfileResponse update(Long userId, WorkerProfileRequest request) {
        WorkerProfile profile = workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found for user: " + userId));

        profile.setDescription(request.getDescription());
        profile.setPricePerHour(request.getPricePerHour());
        profile.setWorkingRadiusKm(request.getWorkingRadiusKm());
        profile.setSkills(resolveSkills(request.getSkillIds()));

        return toResponse(workerProfileRepository.save(profile));
    }

    @Override
    public WorkerProfileResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public WorkerProfileResponse getByUserId(Long userId) {
        return toResponse(workerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found for user: " + userId)));
    }

    @Override
    public List<WorkerProfileResponse> getAll() {
        return workerProfileRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<WorkerProfileResponse> getVerified() {
        return workerProfileRepository.findByIsVerifiedTrue().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<WorkerProfileResponse> getBySkill(Long skillId) {
        return workerProfileRepository.findBySkillIdAndVerified(skillId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<WorkerProfileResponse> getByPriceRange(Double min, Double max) {
        return workerProfileRepository.findByPriceRange(min, max).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WorkerProfileResponse verify(Long id) {
        WorkerProfile profile = findById(id);
        profile.setIsVerified(true);
        return toResponse(workerProfileRepository.save(profile));
    }

    @Override
    public void delete(Long id) {
        findById(id);
        workerProfileRepository.deleteById(id);
    }

    private WorkerProfile findById(Long id) {
        return workerProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Worker profile not found: " + id));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    private List<Skill> resolveSkills(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return ids.stream()
                .map(id -> skillRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + id)))
                .collect(Collectors.toList());
    }

    public WorkerProfileResponse toResponse(WorkerProfile p) {
        List<SkillResponse> skills = p.getSkills() == null ? List.of() :
                p.getSkills().stream()
                        .map(s -> SkillResponse.builder().id(s.getId()).name(s.getName()).build())
                        .collect(Collectors.toList());
        return WorkerProfileResponse.builder()
                .id(p.getId())
                .userId(p.getUser().getId())
                .name(p.getUser().getName())
                .email(p.getUser().getEmail())
                .phone(p.getUser().getPhone())
                .description(p.getDescription())
                .ratingAvg(p.getRatingAvg())
                .totalJobs(p.getTotalJobs())
                .isVerified(p.getIsVerified())
                .pricePerHour(p.getPricePerHour())
                .workingRadiusKm(p.getWorkingRadiusKm())
                .skills(skills)
                .build();
    }
}
