package com.querisek.expensetracker.domain.income;

import java.time.LocalDateTime;
import java.util.UUID;

public class IncomeDeletedEvent {
    private UUID incomeId;
    private String userId;
    private LocalDateTime incomeDeletedAt;

    public IncomeDeletedEvent() {}

    public IncomeDeletedEvent(Income income) {
        this.incomeId = income.getIncomeId();
        this.userId = income.getUserId();
        this.incomeDeletedAt = income.getIncomeCreatedAt();
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

    public LocalDateTime getIncomeDeletedAt() {
        return incomeDeletedAt;
    }

    public void setIncomeDeletedAt(LocalDateTime incomeDeletedAt) {
        this.incomeDeletedAt = incomeDeletedAt;
    }
}
