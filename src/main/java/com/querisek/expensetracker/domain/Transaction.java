package com.querisek.expensetracker.domain;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Transaction {
    private final UUID id;
    private final String description;
    private final double price;
    private final LocalDate createdAt;

    protected Transaction(UUID id, String description, double price, LocalDate createdAt) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }
}
