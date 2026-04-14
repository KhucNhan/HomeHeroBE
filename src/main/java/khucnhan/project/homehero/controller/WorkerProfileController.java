package khucnhan.project.homehero.controller;

import jakarta.validation.Valid;
import khucnhan.project.homehero.dto.request.WorkerProfileRequest;
import khucnhan.project.homehero.dto.response.WorkerProfileResponse;
import khucnhan.project.homehero.service.UserService;
import khucnhan.project.homehero.service.WorkerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerProfileController {

    private final WorkerProfileService workerProfileService;
    private final UserService userService;

    private Long getCurrentUserId(UserDetails userDetails) {
        return userService.getByEmail(userDetails.getUsername()).getId();
    }

    @GetMapping
    public ResponseEntity<List<WorkerProfileResponse>> getVerified() {
        return ResponseEntity.ok(workerProfileService.getVerified());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WorkerProfileResponse>> getAll() {
        return ResponseEntity.ok(workerProfileService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkerProfileResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workerProfileService.getById(id));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(workerProfileService.getByUserId(userId));
    }

    @GetMapping("/filter/skill")
    public ResponseEntity<List<WorkerProfileResponse>> getBySkill(@RequestParam Long skillId) {
        return ResponseEntity.ok(workerProfileService.getBySkill(skillId));
    }

    @GetMapping("/filter/price")
    public ResponseEntity<List<WorkerProfileResponse>> getByPriceRange(@RequestParam Double min,
                                                                       @RequestParam Double max) {
        return ResponseEntity.ok(workerProfileService.getByPriceRange(min, max));
    }

    @PostMapping
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> create(@AuthenticationPrincipal UserDetails userDetails,
                                                        @Valid @RequestBody WorkerProfileRequest request) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(workerProfileService.create(userId, request));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('WORKER')")
    public ResponseEntity<WorkerProfileResponse> update(@AuthenticationPrincipal UserDetails userDetails,
                                                        @Valid @RequestBody WorkerProfileRequest request) {
        Long userId = getCurrentUserId(userDetails);
        return ResponseEntity.ok(workerProfileService.update(userId, request));
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WorkerProfileResponse> verify(@PathVariable Long id) {
        return ResponseEntity.ok(workerProfileService.verify(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workerProfileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}