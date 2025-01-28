package com.querisek.expensetracker.domain.expense;

import com.querisek.expensetracker.domain.common.Category;
import com.querisek.expensetracker.domain.common.Date;
import com.querisek.expensetracker.domain.common.Description;
import com.querisek.expensetracker.domain.common.Money;

import java.time.LocalDate;

public final class AddExpenseRequest {
    private Category category;
    private Description description;
    private Money price;
    private Date createdAt;

    public AddExpenseRequest() {}

    public AddExpenseRequest(String category, String description, double price) {
        this.category = new Category(category);
        this.description = new Description(description);
        this.price = new Money(price);
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
