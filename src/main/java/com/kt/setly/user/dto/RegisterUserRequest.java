package com.kt.setly.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6, max = 100) String password,
        @NotBlank @Size(max = 100) String displayName,
        @NotBlank @Size(min = 3, max = 3) String defaultCurrency,
        @NotBlank @Size(max = 10) String locale,
        @NotBlank @Size(max = 5) String countryCode
) {
}
