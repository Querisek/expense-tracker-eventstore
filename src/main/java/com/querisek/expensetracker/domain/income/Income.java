package com.querisek.expensetracker.domain.income;

import java.time.LocalDateTime;
import java.util.UUID;

public class Income {
    private final UUID incomeId;
    private final String userId;
    private final String incomeDescription;
    private final double price;
    private final LocalDateTime incomeCreatedAt;

    public Income(String userId, String incomeDescription, double price) {
        this.incomeId = UUID.randomUUID();
        this.userId = userId;
        this.incomeDescription = incomeDescription;
        this.price = price;
        this.incomeCreatedAt = LocalDateTime.now();
    }

    public UUID getIncomeId() {
        return incomeId;
    }

    public String getUserId() {
        return userId;
    }

    public String getIncomeDescription() {
        return incomeDescription;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getIncomeCreatedAt() {
        return incomeCreatedAt;
    }
}
