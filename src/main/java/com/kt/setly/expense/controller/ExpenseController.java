package com.kt.setly.expense.controller;

import com.kt.setly.common.response.ApiResponse;
import com.kt.setly.expense.dto.CreateExpenseRequest;
import com.kt.setly.expense.dto.ExpenseResponse;
import com.kt.setly.expense.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/expenses")
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "Expense APIs")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Create expense in group")
    public ApiResponse<ExpenseResponse> createExpense(@PathVariable Long groupId,
                                                      @Valid @RequestBody CreateExpenseRequest request) {
        return ApiResponse.<ExpenseResponse>builder()
                .success(true)
                .message("Expense created successfully")
                .data(expenseService.createExpense(groupId, request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all expenses by group")
    public ApiResponse<List<ExpenseResponse>> getExpenses(@PathVariable Long groupId) {
        return ApiResponse.<List<ExpenseResponse>>builder()
                .success(true)
                .message("Expenses fetched successfully")
                .data(expenseService.getExpenses(groupId))
                .build();
    }

    @GetMapping("/{expenseId}")
    @Operation(summary = "Get expense by id")
    public ApiResponse<ExpenseResponse> getExpense(@PathVariable Long groupId,
                                                   @PathVariable Long expenseId) {
        return ApiResponse.<ExpenseResponse>builder()
                .success(true)
                .message("Expense fetched successfully")
                .data(expenseService.getExpense(groupId, expenseId))
                .build();
    }
}
