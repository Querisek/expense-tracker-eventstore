package com.querisek.expensetracker.domain.expense;

import java.time.LocalDateTime;
import java.util.UUID;

public class Expense {
    private final UUID expenseId;
    private final String userId;
    private final String expenseCategory;
    private final String expenseDescription;
    private final double price;
    private final LocalDateTime expenseCreatedAt;

    public Expense(String userId, String expenseCategory, String expenseDescription, double price) {
        this.expenseId = UUID.randomUUID();
        this.userId = userId;
        this.expenseCategory = expenseCategory;
        this.expenseDescription = expenseDescription;
        this.price = price;
        this.expenseCreatedAt = LocalDateTime.now();
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
