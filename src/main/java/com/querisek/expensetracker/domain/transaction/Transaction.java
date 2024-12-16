package com.querisek.expensetracker.domain.transaction;

import com.querisek.expensetracker.domain.values.Date;
import com.querisek.expensetracker.domain.values.Description;
import com.querisek.expensetracker.domain.values.Money;

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
        return description.getValue();
    }

    public double getPrice() {
        return price.getValue();
    }

    public LocalDate getCreatedAt() {
        return createdAt.getValue();
    }
}
