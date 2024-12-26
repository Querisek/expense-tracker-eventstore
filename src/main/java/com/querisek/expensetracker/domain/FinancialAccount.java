package com.querisek.expensetracker.domain;

import com.querisek.expensetracker.domain.snapshot.MonthlySnapshot;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.income.Income;
import com.querisek.expensetracker.domain.transaction.Transaction;
import com.querisek.expensetracker.domain.transaction.TransactionAddedEvent;
import com.querisek.expensetracker.domain.transaction.TransactionRemovedEvent;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FinancialAccount {
    private final String userId;
    private final List<Transaction> transactions;
    private Object uncommitedEvent;
    private double previousMonthsTotalExpenses;
    private double previousMonthsTotalIncomes;
    private Map<String, Double> previousMonthsExpensesByCategory;

    public FinancialAccount(String userId) {
        this.userId = userId;
        this.transactions = new ArrayList<>();
        this.previousMonthsTotalExpenses = 0.0;
        this.previousMonthsTotalIncomes = 0.0;
        this.previousMonthsExpensesByCategory = new HashMap<>();
    }

    public void addExpense(String category, String description, double price, LocalDate date) {
        UUID transactionId = UUID.randomUUID();
        uncommitedEvent = new TransactionAddedEvent(
                transactionId,
                userId,
                "EXPENSE",
                category,
                description,
                price,
                date
        );
    }

    public void addExpenseFromEvent(UUID id,String category, String description, double price, LocalDate date) {
        Transaction expense = new Expense(id, category, description, price, date);
        transactions.add(expense);
    }

    public void addIncome(String description, double price, LocalDate date) {
        UUID transactionId = UUID.randomUUID();
        uncommitedEvent = new TransactionAddedEvent(
                transactionId,
                userId,
                "INCOME",
                null,
                description,
                price,
                date
        );
    }

    public void addIncomeFromEvent(UUID id, String description, double price, LocalDate date) {
        Transaction income = new Income(id, description, price, date);
        transactions.add(income);
    }

    public void removeTransaction(UUID transactionId) {
        boolean transactionExists = transactions.stream()
                .anyMatch(transaction -> transaction.getId().equals(transactionId));
        if(transactionExists) {
            uncommitedEvent = new TransactionRemovedEvent(
                    transactionId,
                    userId,
                    LocalDate.now()
            );
            transactions.removeIf(transaction -> transaction.getId().equals(transactionId));
        }
    }

    public void loadFromSnapshot(double totalExpenses, double totalIncomes, Map<String, Double> expensesByCategory) {
        this.previousMonthsTotalExpenses = totalExpenses;
        this.previousMonthsTotalIncomes = totalIncomes;
        this.previousMonthsExpensesByCategory = new HashMap<>(expensesByCategory);
    }

    public double getCurrentMonthExpenses() {
        return transactions.stream()
                .filter(t -> t instanceof Expense)
                .mapToDouble(Transaction::getPrice)
                .sum();
    }

    public double getCurrentMonthIncomes() {
        return transactions.stream()
                .filter(t -> t instanceof Income)
                .mapToDouble(Transaction::getPrice)
                .sum();
    }

    public Map<String, Double> getCurrentMonthExpensesByCategory() {
        return transactions.stream()
                .filter(t -> t instanceof Expense)
                .map(t -> (Expense) t)
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Transaction::getPrice)
                ));
    }

    public double getTotalExpenses() {
        return getCurrentMonthExpenses() + previousMonthsTotalExpenses;
    }

    public double getTotalIncomes() {
        return getCurrentMonthIncomes() + previousMonthsTotalIncomes;
    }

    public Map<String, Double> getTotalExpensesByCategory() {
        Map<String, Double> currentMonthCategories = getCurrentMonthExpensesByCategory();
        Map<String, Double> totalCategories = new HashMap<>(previousMonthsExpensesByCategory);

        currentMonthCategories.forEach((category, amount) ->
                totalCategories.merge(category, amount, Double::sum)
        );

        return totalCategories;
    }

    public String getUserId() {
        return userId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Object getUncommitedEvent() {
        return uncommitedEvent;
    }
}
