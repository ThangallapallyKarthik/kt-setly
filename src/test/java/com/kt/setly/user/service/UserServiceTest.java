package com.kt.setly.user.service;

import com.kt.setly.common.exception.BadRequestException;
import com.kt.setly.security.JwtTokenProvider;
import com.kt.setly.user.dto.LoginRequest;
import com.kt.setly.user.dto.LoginResponse;
import com.kt.setly.user.dto.RegisterUserRequest;
import com.kt.setly.user.dto.UserResponse;
import com.kt.setly.user.entity.User;
import com.kt.setly.user.entity.UserStatus;
import com.kt.setly.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private RegisterUserRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .passwordHash("hashedPassword")
                .displayName("Test User")
                .defaultCurrency("USD")
                .locale("en_US")
                .countryCode("US")
                .status(UserStatus.ACTIVE)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        registerRequest = new RegisterUserRequest(
                "newuser@example.com",
                "password123",
                "New User",
                "USD",
                "en_US",
                "US"
        );

        loginRequest = new LoginRequest("test@example.com", "password123");
    }

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUserSuccess() {
        // Arrange
        when(userRepository.existsByEmailIgnoreCase(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtTokenProvider.generateToken(testUser.getId(), testUser.getEmail()))
                .thenReturn("test-jwt-token");

        // Act
        LoginResponse response = userService.register(registerRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.token()).isEqualTo("test-jwt-token");
        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtTokenProvider, times(1)).generateToken(testUser.getId(), testUser.getEmail());
    }

    @Test
    @DisplayName("Should fail when email already exists")
    void testRegisterUserEmailExists() {
        // Arrange
        when(userRepository.existsByEmailIgnoreCase(registerRequest.email())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.register(registerRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Email already exists");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should login user successfully")
    void testLoginUserSuccess() {
        // Arrange
        when(userRepository.findByEmailIgnoreCase(loginRequest.email()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginRequest.password(), testUser.getPasswordHash()))
                .thenReturn(true);
        when(jwtTokenProvider.generateToken(testUser.getId(), testUser.getEmail()))
                .thenReturn("test-jwt-token");

        // Act
        LoginResponse response = userService.login(loginRequest);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.displayName()).isEqualTo("Test User");
        assertThat(response.token()).isEqualTo("test-jwt-token");
    }

    @Test
    @DisplayName("Should fail login with invalid email")
    void testLoginUserNotFound() {
        // Arrange
        when(userRepository.findByEmailIgnoreCase(loginRequest.email()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    @DisplayName("Should fail login with invalid password")
    void testLoginInvalidPassword() {
        // Arrange
        when(userRepository.findByEmailIgnoreCase(loginRequest.email()))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(loginRequest.password(), testUser.getPasswordHash()))
                .thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Invalid email or password");
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void testGetUserByIdSuccess() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        UserResponse response = userService.getUser(1L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        // Arrange
        List<User> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserResponse> responses = userService.getAllUsers();

        // Assert
        assertThat(responses).isNotEmpty();
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).email()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should use default values for optional fields")
    void testRegisterWithDefaultValues() {
        // Arrange
        RegisterUserRequest requestWithNulls = new RegisterUserRequest(
                "user@example.com",
                "password123",
                "User",
                null,
                null,
                null
        );

        when(userRepository.existsByEmailIgnoreCase(requestWithNulls.email())).thenReturn(false);
        when(passwordEncoder.encode(requestWithNulls.password())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtTokenProvider.generateToken(any(), any())).thenReturn("token");

        // Act
        LoginResponse response = userService.register(requestWithNulls);

        // Assert
        assertThat(response).isNotNull();
        verify(userRepository).save(argThat(user ->
                "USD".equals(user.getDefaultCurrency()) &&
                "en_US".equals(user.getLocale()) &&
                "US".equals(user.getCountryCode())
        ));
    }
}

