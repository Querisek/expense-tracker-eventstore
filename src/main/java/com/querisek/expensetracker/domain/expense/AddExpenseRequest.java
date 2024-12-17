package com.querisek.expensetracker.domain.expense;

import com.querisek.expensetracker.domain.common.Category;
import com.querisek.expensetracker.domain.common.Date;
import com.querisek.expensetracker.domain.common.Description;
import com.querisek.expensetracker.domain.common.Money;

import java.time.LocalDate;

public class AddExpenseRequest {
    private Category expenseCategory;
    private Description expenseDescription;
    private Money price;
    private Date expenseCreatedAt;

    public AddExpenseRequest() {}

    public AddExpenseRequest(String expenseCategory, String expenseDescription, double price) {
        this.expenseCategory = new Category(expenseCategory);
        this.expenseDescription = new Description(expenseDescription);
        this.price = new Money(price);
    }

    public String getExpenseCategory() {
        return expenseCategory.getValue();
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = new Category(expenseCategory);
    }

    public String getExpenseDescription() {
        return expenseDescription.getValue();
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = new Description(expenseDescription);
    }

    public double getPrice() {
        return price.getValue();
    }

    public void setPrice(double price) {
        this.price = new Money(price);
    }

    public LocalDate getExpenseCreatedAt() {
        return expenseCreatedAt.getValue();
    }

    public void setExpenseCreatedAt(LocalDate expenseCreatedAt) {
        this.expenseCreatedAt = new Date(expenseCreatedAt);
    }
}
