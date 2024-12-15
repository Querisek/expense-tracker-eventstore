package com.querisek.expensetracker.domain.income;

import java.time.LocalDate;

public class AddIncomeRequest {
    private String incomeDescription;
    private double price;
    private LocalDate incomeCreatedAt;

    public AddIncomeRequest() {}

    public AddIncomeRequest(String incomeDescription, double price) {
        this.incomeDescription = incomeDescription;
        this.price = price;
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

    public LocalDate getIncomeCreatedAt() {
        return incomeCreatedAt;
    }

    public void setIncomeCreatedAt(LocalDate incomeCreatedAt) {
        this.incomeCreatedAt = incomeCreatedAt;
    }
}
