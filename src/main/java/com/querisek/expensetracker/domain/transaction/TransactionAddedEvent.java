package com.querisek.expensetracker.domain.transaction;

import java.time.LocalDate;
import java.util.UUID;

public class TransactionAddedEvent {
    private UUID transactionId;
    private String userId;
    private String type;
    private String category;
    private String description;
    private double price;
    private LocalDate createdAt;

    public TransactionAddedEvent() {}

    public TransactionAddedEvent(UUID transactionId, String userId, String type, String category, String description, double price, LocalDate createdAt) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.category = category;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
