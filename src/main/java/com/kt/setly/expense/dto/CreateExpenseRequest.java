package com.kt.setly.expense.dto;

import com.kt.setly.expense.entity.SplitType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateExpenseRequest(
        @NotBlank @Size(max = 150) String title,
        @Size(max = 500) String description,
        @NotNull Long paidByUserId,
        @NotNull Long createdByUserId,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        @NotNull LocalDate expenseDate,
        @NotNull SplitType splitType,
        @NotEmpty List<@Valid ExpenseParticipantRequest> participants
) {
}
