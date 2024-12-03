package com.querisek.expensetracker.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class ExpenseCreatedEvent {
    private final UUID expenseId;
    private final String userId;
    private final String expenseCategory;
    private final String expenseDescription;
    private final double price;
    private final LocalDateTime expenseCreatedAt;

    public ExpenseCreatedEvent(Expense expense) {
        this.expenseId = expense.getExpenseId();
        this.userId = expense.getUserId();
        this.expenseCategory = expense.getExpenseCategory();
        this.expenseDescription = expense.getExpenseDescription();
        this.price = expense.getPrice();
        this.expenseCreatedAt = expense.getExpenseCreatedAt();
    }

    public UUID getExpenseId() {
        return expenseId;
    }

    public String getUserId() {
        return userId;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getExpenseCreatedAt() {
        return expenseCreatedAt;
    }
}
