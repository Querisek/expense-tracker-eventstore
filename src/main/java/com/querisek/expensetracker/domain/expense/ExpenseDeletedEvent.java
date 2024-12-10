package com.querisek.expensetracker.domain.expense;

import java.time.LocalDateTime;
import java.util.UUID;

public class ExpenseDeletedEvent {
    private UUID expenseId;
    private String userId;
    private LocalDateTime expenseDeletedAt;

    public ExpenseDeletedEvent() {}

    public ExpenseDeletedEvent(Expense expense) {
        this.expenseId = expense.getExpenseId();
        this.userId = expense.getUserId();
        this.expenseDeletedAt = LocalDateTime.now();
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

    public LocalDateTime getExpenseRemovedAt() {
        return expenseDeletedAt;
    }

    public void setExpenseRemovedAt(LocalDateTime expenseRemovedAt) {
        this.expenseDeletedAt = expenseRemovedAt;
    }
}
