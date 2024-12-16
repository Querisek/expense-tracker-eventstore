package com.querisek.expensetracker.domain;

import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.income.Income;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public Transaction addExpense(String category, String description, double price, LocalDate date) {
        Transaction expense = new Expense(UUID.randomUUID(), category, description, price, date);
        transactions.add(expense);
        return expense;
    }

    public Transaction addIncome(String description, double price, LocalDate date) {
        Transaction income = new Income(UUID.randomUUID(), description, price, date);
        transactions.add(income);
        return income;
    }

    public void removeTransaction(UUID transactionId) {
        transactions.removeIf(transaction -> transaction.getId().equals(transactionId));
    }

    public String getUserId() {
        return userId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
