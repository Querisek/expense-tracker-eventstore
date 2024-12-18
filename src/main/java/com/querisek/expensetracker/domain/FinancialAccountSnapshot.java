package com.querisek.expensetracker.domain;

import com.querisek.expensetracker.domain.transaction.Transaction;

import java.util.List;

public class FinancialAccountSnapshot {
    private final String userId;
    private final List<Transaction> transactions;
    private final long version;

    public FinancialAccountSnapshot(String userId, List<Transaction> transactions, long version) {
        this.userId = userId;
        this.transactions = transactions;
        this.version = version;
    }

    public String getUserId() {
        return userId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public long getVersion() {
        return version;
    }
}
