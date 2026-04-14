package khucnhan.project.homehero.service.impl;

import khucnhan.project.homehero.dto.request.LoginRequest;
import khucnhan.project.homehero.dto.request.RegisterRequest;
import khucnhan.project.homehero.dto.response.AuthResponse;
import khucnhan.project.homehero.enums.Role;
import khucnhan.project.homehero.enums.Status;
import khucnhan.project.homehero.exception.BadRequestException;
import khucnhan.project.homehero.model.User;
import khucnhan.project.homehero.repository.UserRepository;
import khucnhan.project.homehero.security.JwtUtil;
import khucnhan.project.homehero.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user.getEmail()))
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        if (user.getStatus() == Status.BANNED) {
            throw new BadRequestException("Account is banned");
        }

        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user.getEmail()))
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
