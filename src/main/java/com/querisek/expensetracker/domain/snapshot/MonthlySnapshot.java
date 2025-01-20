package com.querisek.expensetracker.domain.snapshot;

import com.google.common.collect.ImmutableMap;
import com.querisek.expensetracker.domain.FinancialAccount;

import java.time.YearMonth;
import java.util.Map;

public class MonthlySnapshot {
    private String userEmail;
    private YearMonth month;
    private double totalExpenses;
    private double totalIncomes;
    private Map<String, Double> expensesByCategory;

    public MonthlySnapshot() {}

    public MonthlySnapshot(String userEmail, YearMonth month, double totalExpenses, double totalIncomes, Map<String, Double> expensesByCategory) {
        this.userEmail = userEmail;
        this.month = month;
        this.totalExpenses = totalExpenses;
        this.totalIncomes = totalIncomes;
        this.expensesByCategory = expensesByCategory;
    }

    public static MonthlySnapshot createFromAccount(FinancialAccount account, YearMonth month) {
        return new MonthlySnapshot(
                account.getUserEmail(),
                month,
                account.getTotalExpenses(),
                account.getTotalIncomes(),
                account.getTotalExpensesByCategory()
        );
    }

    public String getUserEmail() {
        return userEmail;
    }

    public YearMonth getMonth() {
        return month;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public double getTotalIncomes() {
        return totalIncomes;
    }

    public Map<String, Double> getExpensesByCategory() {
        return ImmutableMap.copyOf(expensesByCategory);
    }
}
