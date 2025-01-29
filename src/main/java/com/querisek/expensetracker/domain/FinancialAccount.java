package com.querisek.expensetracker.domain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.income.Income;
import com.querisek.expensetracker.domain.transaction.Transaction;
import com.querisek.expensetracker.domain.transaction.TransactionAddedEvent;
import com.querisek.expensetracker.domain.transaction.TransactionRemovedEvent;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public final class FinancialAccount {
    private final String userEmail;
    private final List<Transaction> transactions;
    private Object uncommitedEvent;
    private double previousMonthsTotalExpenses;
    private double previousMonthsTotalIncome;
    private Map<String, Double> previousMonthsExpensesByCategory;

    public FinancialAccount(String userEmail) {
        this.userEmail = userEmail;
        this.transactions = new ArrayList<>();
        this.previousMonthsTotalExpenses = 0.0;
        this.previousMonthsTotalIncome = 0.0;
        this.previousMonthsExpensesByCategory = new HashMap<>();
    }

    public void addExpense(String category, String description, double price, LocalDate date) {
        UUID transactionId = UUID.randomUUID();
        uncommitedEvent = new TransactionAddedEvent(transactionId, userEmail, "EXPENSE", category, description, price, date);
    }

    public void addExpenseFromEvent(UUID id, String category, String description, double price, LocalDate date) {
        Transaction expense = new Expense(id, category, description, price, date);
        transactions.add(expense);
    }

    public void addIncome(String description, double price, LocalDate date) {
        UUID transactionId = UUID.randomUUID();
        uncommitedEvent = new TransactionAddedEvent(transactionId, userEmail, "INCOME", null, description, price, date);
    }

    public void addIncomeFromEvent(UUID id, String description, double price, LocalDate date) {
        Transaction income = new Income(id, description, price, date);
        transactions.add(income);
    }

    public void removeTransaction(UUID transactionId) {
        boolean transactionExists = transactions.stream()
                .anyMatch(transaction -> transaction.getId().equals(transactionId));
        if(transactionExists) {
            uncommitedEvent = new TransactionRemovedEvent(transactionId, userEmail, LocalDate.now());
            transactions.removeIf(transaction -> transaction.getId().equals(transactionId));
        }
    }

    public void loadFromSnapshot(double totalExpenses, double totalIncome, ImmutableMap<String, Double> expensesByCategory) {
        this.previousMonthsTotalExpenses = totalExpenses;
        this.previousMonthsTotalIncome = totalIncome;
        this.previousMonthsExpensesByCategory = new HashMap<>(expensesByCategory);
    }

    public double getCurrentMonthExpenses() {
        return transactions.stream()
                .filter(transaction -> transaction instanceof Expense)
                .mapToDouble(Transaction::getPrice)
                .sum();
    }

    public double getCurrentMonthIncome() {
        return transactions.stream()
                .filter(transaction -> transaction instanceof Income)
                .mapToDouble(Transaction::getPrice)
                .sum();
    }

    public Map<String, Double> getCurrentMonthExpensesByCategory() {
        return transactions.stream()
                .filter(transaction -> transaction instanceof Expense)
                .map(transaction -> (Expense) transaction)
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Transaction::getPrice)));
    }

    public double getTotalExpenses() {
        return getCurrentMonthExpenses() + previousMonthsTotalExpenses;
    }

    public double getTotalIncome() {
        return getCurrentMonthIncome() + previousMonthsTotalIncome;
    }

    public ImmutableMap<String, Double> getTotalExpensesByCategory() {
        Map<String, Double> currentMonthCategories = getCurrentMonthExpensesByCategory();
        Map<String, Double> totalCategories = new HashMap<>(previousMonthsExpensesByCategory);
        currentMonthCategories.forEach((category, amount) -> totalCategories.merge(category, amount, Double::sum));

        return ImmutableMap.copyOf(totalCategories);
    }

    public String getUserEmail() {
        return userEmail;
    }

    public ImmutableList<Transaction> getTransactions() {
        return ImmutableList.copyOf(transactions);
    }

    public Object getUncommitedEvent() {
        return uncommitedEvent;
    }
}
