package com.querisek.expensetracker.domain.expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class ExpenseCreatedEvent {
    private UUID expenseId;
    private String userId;
    private String expenseCategory;
    private String expenseDescription;
    private double price;
    private LocalDate expenseCreatedAt;

    public ExpenseCreatedEvent() {}

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

    public void setExpenseId(UUID expenseId) {
        this.expenseId = expenseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getExpenseCreatedAt() {
        return expenseCreatedAt;
    }

    public void setExpenseCreatedAt(LocalDate expenseCreatedAt) {
        this.expenseCreatedAt = expenseCreatedAt;
    }
}
