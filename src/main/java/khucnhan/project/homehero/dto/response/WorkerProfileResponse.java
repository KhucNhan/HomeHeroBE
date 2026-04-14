package khucnhan.project.homehero.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data @Builder
public class WorkerProfileResponse {
    private Long id;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String description;
    private Double ratingAvg;
    private Integer totalJobs;
    private Boolean isVerified;
    private Double pricePerHour;
    private Integer workingRadiusKm;
    private List<SkillResponse> skills;
}