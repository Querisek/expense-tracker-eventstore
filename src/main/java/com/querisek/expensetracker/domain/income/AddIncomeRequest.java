package com.querisek.expensetracker.domain.income;

import com.querisek.expensetracker.domain.common.Date;
import com.querisek.expensetracker.domain.common.Description;
import com.querisek.expensetracker.domain.common.Money;

import java.time.LocalDate;

public class AddIncomeRequest {
    private Description incomeDescription;
    private Money price;
    private Date incomeCreatedAt;

    public AddIncomeRequest() {}

    public AddIncomeRequest(String incomeDescription, double price) {
        this.incomeDescription = new Description(incomeDescription);
        this.price = new Money(price);
    }

    public String getIncomeDescription() {
        return incomeDescription.getValue();
    }

    public void setIncomeDescription(String incomeDescription) {
        this.incomeDescription = new Description(incomeDescription);
    }

    public double getPrice() {
        return price.getValue();
    }

    public void setPrice(double price) {
        this.price = new Money(price);
    }

    public LocalDate getIncomeCreatedAt() {
        return incomeCreatedAt.getValue();
    }

    public void setIncomeCreatedAt(LocalDate incomeCreatedAt) {
        this.incomeCreatedAt = new Date(incomeCreatedAt);
    }
}
