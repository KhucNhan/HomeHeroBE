package khucnhan.project.homehero.service;

import khucnhan.project.homehero.dto.request.LoginRequest;
import khucnhan.project.homehero.dto.request.RegisterRequest;
import khucnhan.project.homehero.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}