package com.kt.setly.settlement.dto;

import com.kt.setly.settlement.entity.SettlementMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateSettlementRequest(
        @NotNull Long fromUserId,
        @NotNull Long toUserId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank String currency,
        @NotNull SettlementMethod paymentMethod,
        @NotNull LocalDate settlementDate
) {
}
