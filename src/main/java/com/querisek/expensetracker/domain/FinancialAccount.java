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

    public void addExpense(String category, String description, double price, LocalDate date) {
        transactions.add(new Expense(UUID.randomUUID(), category, description, price, date));
    }

    public void addIncome(String description, double price, LocalDate date) {
        transactions.add(new Income(UUID.randomUUID(), description, price, date));
    }

    public void removeTransaction(UUID transactionId) {
        transactions.removeIf(t -> t.getId().equals(transactionId));
    }

    public double getBalance() {
        return transactions.stream()
                .mapToDouble(Transaction::getPrice)
                .sum();
    }
}
