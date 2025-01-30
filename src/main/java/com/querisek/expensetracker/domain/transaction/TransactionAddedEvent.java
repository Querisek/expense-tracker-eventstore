package com.querisek.expensetracker.domain.transaction;

import com.querisek.expensetracker.domain.common.Category;
import com.querisek.expensetracker.domain.common.Date;
import com.querisek.expensetracker.domain.common.Description;
import com.querisek.expensetracker.domain.common.Money;

import java.time.LocalDate;
import java.util.UUID;

public final class TransactionAddedEvent {
    private UUID transactionId;
    private String userEmail;
    private String type;
    private Category category;
    private Description description;
    private Money price;
    private Date createdAt;

    public TransactionAddedEvent() {}

    public TransactionAddedEvent(UUID transactionId, String userEmail, String type, String category, String description, double price, LocalDate createdAt) {
        this.transactionId = transactionId;
        this.userEmail = userEmail;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category.getName();
    }

    public void setCategory(String category) {
        this.category = new Category(category);
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
