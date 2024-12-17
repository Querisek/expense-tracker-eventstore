package com.querisek.expensetracker.domain;

import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.income.Income;
import com.querisek.expensetracker.domain.transaction.Transaction;
import com.querisek.expensetracker.domain.transaction.TransactionAddedEvent;
import com.querisek.expensetracker.domain.transaction.TransactionRemovedEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FinancialAccount {
    private final String userId;
    private final List<Transaction> transactions;
    private final List<Object> uncommitedEvents;

    public FinancialAccount(String userId) {
        this.userId = userId;
        this.transactions = new ArrayList<>();
        this.uncommitedEvents = new ArrayList<>();
    }

    public void addExpense(String category, String description, double price, LocalDate date) {
        UUID transactionId = UUID.randomUUID();
        TransactionAddedEvent event = new TransactionAddedEvent(
                transactionId,
                userId,
                "EXPENSE",
                category,
                description,
                price,
                date
        );
        uncommitedEvents.add(event);

        Transaction expense = new Expense(transactionId, category, description, price, date);
        transactions.add(expense);
    }

    public void addExpenseFromEvent(UUID id,String category, String description, double price, LocalDate date) {
        Transaction expense = new Expense(id, category, description, price, date);
        transactions.add(expense);
    }

    public void addIncome(String description, double price, LocalDate date) {
        UUID transactionId = UUID.randomUUID();
        TransactionAddedEvent event = new TransactionAddedEvent(
                transactionId,
                userId,
                "INCOME",
                null,
                description,
                price,
                date
        );
        uncommitedEvents.add(event);

        Transaction income = new Income(transactionId, description, price, date);
        transactions.add(income);
    }

    public void addIncomeFromEvent(UUID id, String description, double price, LocalDate date) {
        Transaction income = new Income(id, description, price, date);
        transactions.add(income);
    }

    public void removeTransaction(UUID transactionId) {
        boolean transactionExists = transactions.stream()
                .anyMatch(transaction -> transaction.getId().equals(transactionId));
        if(transactionExists) {
            TransactionRemovedEvent event = new TransactionRemovedEvent(
                    transactionId,
                    userId,
                    LocalDate.now()
            );
            uncommitedEvents.add(event);

            transactions.removeIf(transaction -> transaction.getId().equals(transactionId));
        }
    }

    public void clearUncommitedEvents() {
        uncommitedEvents.clear();
    }

    public String getUserId() {
        return userId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Object> getUncommitedEvents() {
        return uncommitedEvents;
    }
}
