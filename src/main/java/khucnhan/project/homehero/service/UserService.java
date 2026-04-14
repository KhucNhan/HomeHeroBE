package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.response.UserResponse;
import khucnhan.project.homehero.enums.Status;
import java.util.List;

public interface UserService {
    UserResponse getById(Long id);
    UserResponse getByEmail(String email);
    List<UserResponse> getAll();
    UserResponse updateStatus(Long id, Status status);
    void delete(Long id);
}