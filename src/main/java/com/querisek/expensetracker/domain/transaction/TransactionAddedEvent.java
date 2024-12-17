package com.querisek.expensetracker.domain.transaction;

import com.querisek.expensetracker.domain.common.Category;
import com.querisek.expensetracker.domain.common.Date;
import com.querisek.expensetracker.domain.common.Description;
import com.querisek.expensetracker.domain.common.Money;

import java.time.LocalDate;
import java.util.UUID;

public class TransactionAddedEvent {
    private UUID transactionId;
    private String userId;
    private String type;
    private Category category;
    private Description description;
    private Money price;
    private Date createdAt;

    public TransactionAddedEvent() {}

    public TransactionAddedEvent(UUID transactionId, String userId, String type, String category, String description, double price, LocalDate createdAt) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.category = new Category(category);
        this.description = new Description(description);
        this.price = new Money(price);
        this.createdAt = new Date(createdAt);
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
        return category.getValue();
    }

    public void setCategory(String category) {
        this.category = new Category(category);
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setDescription(String description) {
        this.description = new Description(description);
    }

    public double getPrice() {
        return price.getValue();
    }

    public void setPrice(double price) {
        this.price = new Money(price);
    }

    public LocalDate getCreatedAt() {
        return createdAt.getValue();
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = new Date(createdAt);
    }
}
