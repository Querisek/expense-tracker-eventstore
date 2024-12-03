package com.querisek.expensetracker.domain;

public class AddExpenseRequest {
    private String expenseCategory;
    private String expenseDescription;
    private double price;

    public AddExpenseRequest() {}

    public AddExpenseRequest(String expenseCategory, String expenseDescription, double price) {
        this.expenseCategory = expenseCategory;
        this.expenseDescription = expenseDescription;
        this.price = price;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
