package com.kt.setly.expense.dto;

import java.math.BigDecimal;

public record ExpenseSplitResponse(
        Long userId,
        BigDecimal owedAmount
) {
}
