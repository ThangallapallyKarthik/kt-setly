package com.kt.setly.expense.repository;

import com.kt.setly.expense.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroup_IdOrderByExpenseDateDescIdDesc(Long groupId);
}
