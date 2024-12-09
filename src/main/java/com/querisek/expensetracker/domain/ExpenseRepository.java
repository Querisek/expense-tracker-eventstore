package com.querisek.expensetracker.domain;

import java.util.List;

public interface ExpenseRepository {
    void addExpense(Expense expense);
    List<ExpenseCreatedEvent> listUsersExpensesByCategory(String userId, String categoryName);
    double getTotalPriceOfCategoryByDay(String category, List<ExpenseCreatedEvent> allExpensesByDay);
}
