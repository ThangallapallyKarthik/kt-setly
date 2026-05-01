package com.kt.setly.settlement.service;

import com.kt.setly.balance.service.BalanceService;
import com.kt.setly.common.exception.BadRequestException;
import com.kt.setly.group.entity.ExpenseGroup;
import com.kt.setly.group.service.GroupService;
import com.kt.setly.settlement.dto.CreateSettlementRequest;
import com.kt.setly.settlement.dto.SettlementResponse;
import com.kt.setly.settlement.dto.SettlementSuggestion;
import com.kt.setly.settlement.entity.Settlement;
import com.kt.setly.settlement.entity.SettlementStatus;
import com.kt.setly.settlement.repository.SettlementRepository;
import com.kt.setly.user.entity.User;
import com.kt.setly.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final BalanceService balanceService;

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

    public List<SettlementSuggestion> getSuggestedSettlements(Long groupId) {
        ExpenseGroup group = groupService.getGroupEntity(groupId);
        var balances = balanceService.getGroupBalances(groupId);
        
        Map<Long, BigDecimal> userBalances = new HashMap<>();
        balances.forEach(balance -> userBalances.put(balance.userId(), balance.balance()));

        List<SettlementSuggestion> suggestions = new ArrayList<>();
        
        // Separate debtors and creditors
        List<Map.Entry<Long, BigDecimal>> debtors = new ArrayList<>();
        List<Map.Entry<Long, BigDecimal>> creditors = new ArrayList<>();

        userBalances.forEach((userId, balance) -> {
            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(Map.entry(userId, balance.abs()));
            } else if (balance.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(Map.entry(userId, balance));
            }
        });

        // Match debtors with creditors
        for (Map.Entry<Long, BigDecimal> debtor : debtors) {
            for (Map.Entry<Long, BigDecimal> creditor : creditors) {
                if (debtor.getValue().compareTo(BigDecimal.ZERO) > 0 && 
                    creditor.getValue().compareTo(BigDecimal.ZERO) > 0) {
                    
                    BigDecimal settleAmount = debtor.getValue().min(creditor.getValue());
                    
                    User debtorUser = userService.getUserEntity(debtor.getKey());
                    User creditorUser = userService.getUserEntity(creditor.getKey());
                    
                    suggestions.add(new SettlementSuggestion(
                            debtor.getKey(),
                            debtorUser.getDisplayName(),
                            creditor.getKey(),
                            creditorUser.getDisplayName(),
                            settleAmount,
                            group.getBaseCurrency()
                    ));

                    debtor.setValue(debtor.getValue().subtract(settleAmount));
                    creditor.setValue(creditor.getValue().subtract(settleAmount));
                }
            }
        }

        return suggestions;
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
