package com.kt.setly.balance.service;

import com.kt.setly.balance.dto.UserBalanceResponse;
import com.kt.setly.expense.entity.Expense;
import com.kt.setly.expense.entity.ExpenseSplit;
import com.kt.setly.expense.repository.ExpenseRepository;
import com.kt.setly.expense.repository.ExpenseSplitRepository;
import com.kt.setly.settlement.entity.Settlement;
import com.kt.setly.settlement.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final SettlementRepository settlementRepository;

    public List<UserBalanceResponse> getGroupBalances(Long groupId) {
        Map<Long, BigDecimal> balances = new HashMap<>();

        for (Expense expense : expenseRepository.findByGroup_IdOrderByExpenseDateDescIdDesc(groupId)) {
            balances.merge(expense.getPaidBy().getId(), expense.getAmount(), BigDecimal::add);
            List<ExpenseSplit> splits = expenseSplitRepository.findByExpenseId(expense.getId());
            for (ExpenseSplit split : splits) {
                balances.merge(split.getUser().getId(), split.getOwedAmount().negate(), BigDecimal::add);
            }
        }

        for (Settlement settlement : settlementRepository.findByGroup_IdOrderBySettlementDateDescIdDesc(groupId)) {
            balances.merge(settlement.getFromUser().getId(), settlement.getAmount(), BigDecimal::add);
            balances.merge(settlement.getToUser().getId(), settlement.getAmount().negate(), BigDecimal::add);
        }

        return balances.entrySet().stream()
                .map(entry -> new UserBalanceResponse(entry.getKey(), entry.getValue()))
                .toList();
    }
}
