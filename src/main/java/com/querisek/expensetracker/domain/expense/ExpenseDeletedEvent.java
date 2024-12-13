package com.querisek.expensetracker.domain.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExpenseDeletedEvent {
    private UUID expenseId;
    private String userId;
    private LocalDate expenseDeletedAt;

    public ExpenseDeletedEvent() {}

    public ExpenseDeletedEvent(Expense expense) {
        this.expenseId = expense.getExpenseId();
        this.userId = expense.getUserId();
        this.expenseDeletedAt = LocalDate.now();
    }

    public UUID getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(UUID expenseId) {
        this.expenseId = expenseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getExpenseRemovedAt() {
        return expenseDeletedAt;
    }

    public void setExpenseRemovedAt(LocalDate expenseRemovedAt) {
        this.expenseDeletedAt = expenseRemovedAt;
    }
}
