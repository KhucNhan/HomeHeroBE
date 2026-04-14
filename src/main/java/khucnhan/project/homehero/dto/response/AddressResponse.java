package khucnhan.project.homehero.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class AddressResponse {
    private Long id;
    private String addressLine;
    private Double lat;
    private Double lng;
    private String label;
    private Boolean isDefault;
    private Long userId;
}