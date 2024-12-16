package com.querisek.expensetracker.domain;

import java.util.ArrayList;
import java.util.List;

public class FinancialAccount {
    private final String userId;
    private final List<Transaction> transactions;

    public FinancialAccount(String userId) {
        this.userId = userId;
        this.transactions = new ArrayList<>();
    }

    public FinancialAccount(String userId, List<Transaction> transactions) {
        this.userId = userId;
        this.transactions = new ArrayList<>(transactions);
    }

    public String getUserId() {
        return userId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
