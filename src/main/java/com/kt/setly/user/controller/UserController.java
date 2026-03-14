package com.kt.setly.user.controller;

import com.kt.setly.common.response.ApiResponse;
import com.kt.setly.user.dto.LoginRequest;
import com.kt.setly.user.dto.RegisterUserRequest;
import com.kt.setly.user.dto.UserResponse;
import com.kt.setly.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User registration and profile APIs")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register user")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ApiResponse<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(userService.register(request))
                .build();
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ApiResponse<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Login successful")
                .data(userService.login(request))
                .build();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by id")
    public ApiResponse<UserResponse> getUser(@PathVariable Long userId) {
        return ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User fetched successfully")
                .data(userService.getUser(userId))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .success(true)
                .message("Users fetched successfully")
                .data(userService.getAllUsers())
                .build();
    }
}
