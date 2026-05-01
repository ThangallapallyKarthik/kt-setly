package com.kt.setly.user.dto;

import com.kt.setly.user.entity.UserStatus;

public record LoginResponse(
        Long id,
        String email,
        String displayName,
        String defaultCurrency,
        String locale,
        String countryCode,
        UserStatus status,
        String token
) {
}

