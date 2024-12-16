package com.querisek.expensetracker.domain.income;

import com.querisek.expensetracker.domain.transaction.Transaction;

import java.time.LocalDate;
import java.util.UUID;

public class Income extends Transaction {
    public Income(UUID id, String description, double price, LocalDate createdAt) {
        super(id, description, price, createdAt);
    }
}
