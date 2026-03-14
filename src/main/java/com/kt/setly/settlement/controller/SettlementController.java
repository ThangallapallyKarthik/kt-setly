package com.kt.setly.settlement.controller;

import com.kt.setly.common.response.ApiResponse;
import com.kt.setly.settlement.dto.CreateSettlementRequest;
import com.kt.setly.settlement.dto.SettlementResponse;
import com.kt.setly.settlement.service.SettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/settlements")
@RequiredArgsConstructor
@Tag(name = "Settlements", description = "Settlement APIs")
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    @Operation(summary = "Create settlement")
    public ApiResponse<SettlementResponse> createSettlement(@PathVariable Long groupId,
                                                            @Valid @RequestBody CreateSettlementRequest request) {
        return ApiResponse.<SettlementResponse>builder()
                .success(true)
                .message("Settlement created successfully")
                .data(settlementService.createSettlement(groupId, request))
                .build();
    }

    @GetMapping
    @Operation(summary = "Get settlements by group")
    public ApiResponse<List<SettlementResponse>> getSettlements(@PathVariable Long groupId) {
        return ApiResponse.<List<SettlementResponse>>builder()
                .success(true)
                .message("Settlements fetched successfully")
                .data(settlementService.getSettlements(groupId))
                .build();
    }
}
