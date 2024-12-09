package com.querisek.expensetracker.domain.income;

import com.querisek.expensetracker.domain.expense.ExpenseCreatedEvent;

import java.util.List;

public interface IncomeRepository {
    void addIncome(Income income);
    List<IncomeCreatedEvent> listUsersIncomes(String userId);
}
