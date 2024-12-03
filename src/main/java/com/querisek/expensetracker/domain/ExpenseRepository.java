package com.querisek.expensetracker.domain;

import java.util.List;

public interface ExpenseRepository {
    void addExpense(Expense expense);
    List<ExpenseCreatedEvent> listUsersExpenses(String userId);
}
