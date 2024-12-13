package com.querisek.expensetracker.domain.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Expense {
    private final UUID expenseId;
    private final String userId;
    private final String expenseCategory;
    private final String expenseDescription;
    private final double price;
    private final LocalDate expenseCreatedAt;

    public Expense(String userId, String expenseCategory, String expenseDescription, double price, LocalDate expenseCreatedAt) {
        this.expenseId = UUID.randomUUID();
        this.userId = userId;
        this.expenseCategory = expenseCategory;
        this.expenseDescription = expenseDescription;
        this.price = price;
        this.expenseCreatedAt = Objects.requireNonNullElseGet(expenseCreatedAt, LocalDate::now);
    }

    public Expense(UUID expenseId, String userId, String expenseCategory, String expenseDescription, double price) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.expenseCategory = expenseCategory;
        this.expenseDescription = expenseDescription;
        this.price = price;
        this.expenseCreatedAt = LocalDate.now();
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

    public LocalDate getExpenseCreatedAt() {
        return expenseCreatedAt;
    }
}
