package com.kt.setly.expense.dto;

import com.kt.setly.expense.entity.SplitType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ExpenseResponse(
        Long id,
        Long groupId,
        String title,
        String description,
        Long paidByUserId,
        Long createdByUserId,
        BigDecimal amount,
        String currency,
        LocalDate expenseDate,
        SplitType splitType,
        List<ExpenseSplitResponse> splits
) {
}
