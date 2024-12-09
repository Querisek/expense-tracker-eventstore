package com.querisek.expensetracker.domain.income;

import java.time.LocalDateTime;
import java.util.UUID;

public class IncomeCreatedEvent {
    private UUID incomeId;
    private String userId;
    private String incomeDescription;
    private double price;
    private LocalDateTime incomeCreatedAt;

    public IncomeCreatedEvent() {}

    public IncomeCreatedEvent(Income income) {
        this.incomeId = income.getIncomeId();
        this.userId = income.getUserId();
        this.incomeDescription = income.getIncomeDescription();
        this.price = income.getPrice();
        this.incomeCreatedAt = income.getIncomeCreatedAt();
    }

    public UUID getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(UUID incomeId) {
        this.incomeId = incomeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIncomeDescription() {
        return incomeDescription;
    }

    public void setIncomeDescription(String incomeDescription) {
        this.incomeDescription = incomeDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getIncomeCreatedAt() {
        return incomeCreatedAt;
    }

    public void setIncomeCreatedAt(LocalDateTime incomeCreatedAt) {
        this.incomeCreatedAt = incomeCreatedAt;
    }
}
