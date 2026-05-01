package com.kt.setly.expense.dto;

import com.kt.setly.expense.entity.SplitType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UpdateExpenseRequest(
        @Size(max = 150) String title,
        @Size(max = 500) String description,
        @DecimalMin("0.01") BigDecimal amount,
        @Size(min = 3, max = 3) String currency,
        LocalDate expenseDate,
        SplitType splitType,
        List<@Valid ExpenseParticipantRequest> participants
) {
}

