package khucnhan.project.homehero.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class WorkerProfileRequest {
    private String description;
    @NotNull private Double pricePerHour;
    private Integer workingRadiusKm;
    private List<Long> skillIds;
}