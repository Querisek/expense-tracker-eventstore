package com.querisek.expensetracker.domain.income;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Income {
    private final UUID incomeId;
    private final String userId;
    private final String incomeDescription;
    private final double price;
    private final LocalDate incomeCreatedAt;

    public Income(String userId, String incomeDescription, double price, LocalDate incomeCreatedAt) {
        this.incomeId = UUID.randomUUID();
        this.userId = userId;
        this.incomeDescription = incomeDescription;
        this.price = price;
        this.incomeCreatedAt = Objects.requireNonNullElseGet(incomeCreatedAt, LocalDate::now);
    }

    public Income(UUID incomeId, String userId, String incomeDescription, double price) {
        this.incomeId = incomeId;
        this.userId = userId;
        this.incomeDescription = incomeDescription;
        this.price = price;
        this.incomeCreatedAt = LocalDate.now();
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

    public LocalDate getIncomeCreatedAt() {
        return incomeCreatedAt;
    }
}
