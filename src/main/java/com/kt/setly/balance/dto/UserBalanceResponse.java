package com.kt.setly.balance.dto;

import java.math.BigDecimal;

public record UserBalanceResponse(
        Long userId,
        BigDecimal balance
) {
}
