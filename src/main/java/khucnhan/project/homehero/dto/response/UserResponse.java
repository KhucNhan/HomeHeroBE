package khucnhan.project.homehero.dto.response;

import khucnhan.project.homehero.enums.Role;
import khucnhan.project.homehero.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private Status status;
}