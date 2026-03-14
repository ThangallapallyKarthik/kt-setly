package com.kt.setly.user.service;

import com.kt.setly.common.exception.BadRequestException;
import com.kt.setly.common.exception.ResourceNotFoundException;
import com.kt.setly.user.dto.LoginRequest;
import com.kt.setly.user.dto.RegisterUserRequest;
import com.kt.setly.user.dto.UserResponse;
import com.kt.setly.user.entity.User;
import com.kt.setly.user.entity.UserStatus;
import com.kt.setly.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())) {
            throw new BadRequestException("Email already exists");
        }

        OffsetDateTime now = OffsetDateTime.now();
        User user = User.builder()
                .email(request.email().trim().toLowerCase())
                .passwordHash(passwordEncoder.encode(request.password()))
                .displayName(request.displayName().trim())
//                .defaultCurrency(request.defaultCurrency().trim().toUpperCase())
//                .locale(request.locale().trim())
//                .countryCode(request.countryCode().trim().toUpperCase())
                .defaultCurrency(request.defaultCurrency() != null
                        ? request.defaultCurrency().trim().toUpperCase()
                        : "USD")
                .locale(request.locale() != null
                        ? request.locale().trim()
                        : "en_US")
                .countryCode(request.countryCode() != null
                        ? request.countryCode().trim().toUpperCase()
                        : "US")
                .status(UserStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return map(userRepository.save(user));
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadRequestException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BadRequestException("Invalid email or password");
        }

        return map(user);
    }

    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id: " + userId));
    }

    public UserResponse getUser(Long userId) {
        return map(getUserEntity(userId));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::map).toList();
    }

    private UserResponse map(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getDefaultCurrency(),
                user.getLocale(),
                user.getCountryCode(),
                user.getStatus()
        );
    }
}
