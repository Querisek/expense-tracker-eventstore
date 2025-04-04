package com.querisek.expensetracker.domain.transaction;

import com.querisek.expensetracker.domain.common.Date;
import com.querisek.expensetracker.domain.common.Description;
import com.querisek.expensetracker.domain.common.Money;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Transaction {
    private final UUID id;
    private final Description description;
    private final Money price;
    private final Date createdAt;

    protected Transaction(UUID id, String description, double price, LocalDate createdAt) {
        this.id = id;
        this.description = new Description(description);
        this.price = new Money(price);
        this.createdAt = new Date(createdAt);
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description.getDescription();
    }

    public double getPrice() {
        return price.getAmount();
    }

    public LocalDate getCreatedAt() {
        return createdAt.getDate();
    }
}
