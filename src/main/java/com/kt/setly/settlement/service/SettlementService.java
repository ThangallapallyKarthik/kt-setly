package com.kt.setly.settlement.service;

import com.kt.setly.common.exception.BadRequestException;
import com.kt.setly.group.entity.ExpenseGroup;
import com.kt.setly.group.service.GroupService;
import com.kt.setly.settlement.dto.CreateSettlementRequest;
import com.kt.setly.settlement.dto.SettlementResponse;
import com.kt.setly.settlement.entity.Settlement;
import com.kt.setly.settlement.entity.SettlementStatus;
import com.kt.setly.settlement.repository.SettlementRepository;
import com.kt.setly.user.entity.User;
import com.kt.setly.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final GroupService groupService;
    private final UserService userService;

    public SettlementResponse createSettlement(Long groupId, CreateSettlementRequest request) {
        if (request.fromUserId().equals(request.toUserId())) {
            throw new BadRequestException("fromUserId and toUserId cannot be the same");
        }

        groupService.validateMember(groupId, request.fromUserId());
        groupService.validateMember(groupId, request.toUserId());

        ExpenseGroup group = groupService.getGroupEntity(groupId);
        User fromUser = userService.getUserEntity(request.fromUserId());
        User toUser = userService.getUserEntity(request.toUserId());

        Settlement saved = settlementRepository.save(Settlement.builder()
                .group(group)
                .fromUser(fromUser)
                .toUser(toUser)
                .amount(request.amount())
                .currency(request.currency().trim().toUpperCase())
                .paymentMethod(request.paymentMethod())
                .status(SettlementStatus.COMPLETED)
                .settlementDate(request.settlementDate())
                .createdAt(OffsetDateTime.now())
                .build());

        return map(saved);
    }

    public List<SettlementResponse> getSettlements(Long groupId) {
        return settlementRepository.findByGroup_IdOrderBySettlementDateDescIdDesc(groupId).stream()
                .map(this::map)
                .toList();
    }

    private SettlementResponse map(Settlement settlement) {
        return new SettlementResponse(
                settlement.getId(),
                settlement.getGroup().getId(),
                settlement.getFromUser().getId(),
                settlement.getToUser().getId(),
                settlement.getAmount(),
                settlement.getCurrency(),
                settlement.getPaymentMethod(),
                settlement.getStatus(),
                settlement.getSettlementDate()
        );
    }
}
