package khucnhan.project.homehero.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank private String addressLine;
    private Double lat;
    private Double lng;
    private String label;
    private Boolean isDefault = false;
}