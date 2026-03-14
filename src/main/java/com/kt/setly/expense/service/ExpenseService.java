package com.kt.setly.expense.service;

import com.kt.setly.common.exception.BadRequestException;
import com.kt.setly.common.exception.ResourceNotFoundException;
import com.kt.setly.expense.dto.*;
import com.kt.setly.expense.entity.Expense;
import com.kt.setly.expense.entity.ExpenseSplit;
import com.kt.setly.expense.repository.ExpenseRepository;
import com.kt.setly.expense.repository.ExpenseSplitRepository;
import com.kt.setly.group.entity.ExpenseGroup;
import com.kt.setly.group.service.GroupService;
import com.kt.setly.user.entity.User;
import com.kt.setly.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final GroupService groupService;
    private final UserService userService;

    @Transactional
    public ExpenseResponse createExpense(Long groupId, CreateExpenseRequest request) {
        validateSplitTotals(request);
        ExpenseGroup group = groupService.getGroupEntity(groupId);
        groupService.validateMember(groupId, request.paidByUserId());
        groupService.validateMember(groupId, request.createdByUserId());
        request.participants().forEach(participant -> groupService.validateMember(groupId, participant.userId()));

        User paidBy = userService.getUserEntity(request.paidByUserId());
        User createdBy = userService.getUserEntity(request.createdByUserId());
        OffsetDateTime now = OffsetDateTime.now();

        Expense expense = Expense.builder()
                .group(group)
                .title(request.title().trim())
                .description(request.description())
                .paidBy(paidBy)
                .createdBy(createdBy)
                .amount(request.amount())
                .currency(request.currency().trim().toUpperCase())
                .expenseDate(request.expenseDate())
                .splitType(request.splitType())
                .createdAt(now)
                .updatedAt(now)
                .build();

        Expense savedExpense = expenseRepository.save(expense);

        List<ExpenseSplit> splits = request.participants().stream()
                .map(participant -> ExpenseSplit.builder()
                        .expense(savedExpense)
                        .user(userService.getUserEntity(participant.userId()))
                        .owedAmount(participant.owedAmount())
                        .build())
                .toList();
        expenseSplitRepository.saveAll(splits);

        return map(savedExpense, splits);
    }

    public List<ExpenseResponse> getExpenses(Long groupId) {
        return expenseRepository.findByGroup_IdOrderByExpenseDateDescIdDesc(groupId).stream()
                .map(expense -> map(expense, expenseSplitRepository.findByExpenseId(expense.getId())))
                .toList();
    }

    public ExpenseResponse getExpense(Long groupId, Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .filter(item -> item.getGroup().getId().equals(groupId))
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found for id: " + expenseId));

        return map(expense, expenseSplitRepository.findByExpenseId(expense.getId()));
    }

    private void validateSplitTotals(CreateExpenseRequest request) {
        BigDecimal total = request.participants().stream()
                .map(ExpenseParticipantRequest::owedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(request.amount()) != 0) {
            throw new BadRequestException("Split total must exactly match expense amount");
        }
    }

    private ExpenseResponse map(Expense expense, List<ExpenseSplit> splits) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getGroup().getId(),
                expense.getTitle(),
                expense.getDescription(),
                expense.getPaidBy().getId(),
                expense.getCreatedBy().getId(),
                expense.getAmount(),
                expense.getCurrency(),
                expense.getExpenseDate(),
                expense.getSplitType(),
                splits.stream().map(split -> new ExpenseSplitResponse(split.getUser().getId(), split.getOwedAmount())).toList()
        );
    }
}
