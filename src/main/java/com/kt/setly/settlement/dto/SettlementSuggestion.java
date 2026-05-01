package com.kt.setly.settlement.dto;

import java.math.BigDecimal;

public record SettlementSuggestion(
        Long fromUserId,
        String fromUserName,
        Long toUserId,
        String toUserName,
        BigDecimal amount,
        String currency
) {
}

