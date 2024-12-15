package com.querisek.expensetracker.domain.expense;

import com.querisek.expensetracker.domain.Transaction;

import java.time.LocalDate;
import java.util.UUID;

public class Expense extends Transaction {
    private final String category;

    public Expense(UUID id, String category, String description, double price, LocalDate createdAt) {
        super(id, description, price, createdAt);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
