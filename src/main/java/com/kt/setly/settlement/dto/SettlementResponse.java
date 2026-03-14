package com.kt.setly.settlement.dto;

import com.kt.setly.settlement.entity.SettlementMethod;
import com.kt.setly.settlement.entity.SettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SettlementResponse(
        Long id,
        Long groupId,
        Long fromUserId,
        Long toUserId,
        BigDecimal amount,
        String currency,
        SettlementMethod paymentMethod,
        SettlementStatus status,
        LocalDate settlementDate
) {
}
