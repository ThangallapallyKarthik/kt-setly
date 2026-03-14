package com.kt.setly.balance.controller;

import com.kt.setly.balance.dto.UserBalanceResponse;
import com.kt.setly.balance.service.BalanceService;
import com.kt.setly.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/balances")
@RequiredArgsConstructor
@Tag(name = "Balances", description = "Balance calculation APIs")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping
    @Operation(summary = "Get group balances")
    public ApiResponse<List<UserBalanceResponse>> getGroupBalances(@PathVariable Long groupId) {
        return ApiResponse.<List<UserBalanceResponse>>builder()
                .success(true)
                .message("Balances fetched successfully")
                .data(balanceService.getGroupBalances(groupId))
                .build();
    }
}
