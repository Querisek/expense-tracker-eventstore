package com.querisek.expensetracker.domain.income;

import com.querisek.expensetracker.domain.common.Date;
import com.querisek.expensetracker.domain.common.Description;
import com.querisek.expensetracker.domain.common.Money;

import java.time.LocalDate;

public final class AddIncomeRequest {
    private Description description;
    private Money price;
    private Date createdAt;

    public AddIncomeRequest() {}

    public AddIncomeRequest(String description, double price) {
        this.description = new Description(description);
        this.price = new Money(price);
    }

    public String getDescription() {
        return description.getDescription();
    }

    public void setDescription(String description) {
        this.description = new Description(description);
    }

    public double getPrice() {
        return price.getAmount();
    }

    public void setPrice(double price) {
        this.price = new Money(price);
    }

    public LocalDate getCreatedAt() {
        return createdAt.getDate();
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = new Date(createdAt);
    }
}
