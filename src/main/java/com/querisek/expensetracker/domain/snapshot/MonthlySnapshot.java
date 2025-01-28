package com.querisek.expensetracker.domain.snapshot;

import com.google.common.collect.ImmutableMap;
import com.querisek.expensetracker.domain.FinancialAccount;

import java.time.YearMonth;
import java.util.Map;

public final class MonthlySnapshot {
    private String userEmail;
    private YearMonth yearMonth;
    private double totalExpenses;
    private double totalIncome;
    private Map<String, Double> expensesByCategory;

    public MonthlySnapshot() {}

    public MonthlySnapshot(String userEmail, YearMonth yearMonth, double totalExpenses, double totalIncome, Map<String, Double> expensesByCategory) {
        this.userEmail = userEmail;
        this.yearMonth = yearMonth;
        this.totalExpenses = totalExpenses;
        this.totalIncome = totalIncome;
        this.expensesByCategory = expensesByCategory;
    }

    public static MonthlySnapshot createFromAccount(FinancialAccount account, YearMonth yearMonth) {
        return new MonthlySnapshot(
                account.getUserEmail(),
                yearMonth,
                account.getTotalExpenses(),
                account.getTotalIncome(),
                account.getTotalExpensesByCategory()
        );
    }

    public String getUserEmail() {
        return userEmail;
    }

    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public double getTotalIncome() {
        return totalIncome;
    }

    public Map<String, Double> getExpensesByCategory() {
        return ImmutableMap.copyOf(expensesByCategory);
    }
}
