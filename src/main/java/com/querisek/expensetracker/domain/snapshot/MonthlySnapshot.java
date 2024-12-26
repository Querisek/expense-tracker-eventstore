package com.querisek.expensetracker.domain.snapshot;

import com.querisek.expensetracker.domain.FinancialAccount;

import java.time.YearMonth;
import java.util.Map;

public class MonthlySnapshot {
    private String userId;
    private YearMonth month;
    private double totalExpenses;
    private double totalIncomes;
    private Map<String, Double> expensesByCategory;

    public MonthlySnapshot() {}

    public MonthlySnapshot(String userId, YearMonth month, double totalExpenses, double totalIncomes, Map<String, Double> expensesByCategory) {
        this.userId = userId;
        this.month = month;
        this.totalExpenses = totalExpenses;
        this.totalIncomes = totalIncomes;
        this.expensesByCategory = expensesByCategory;
    }

    public static MonthlySnapshot createFromAccount(FinancialAccount account, YearMonth month) {
        double monthlyTotalExpenses = account.getCurrentMonthExpenses();
        double monthlyTotalIncomes = account.getCurrentMonthIncomes();
        Map<String, Double> expensesByCategory = account.getCurrentMonthExpensesByCategory();

        return new MonthlySnapshot(
                account.getUserId(),
                month,
                monthlyTotalExpenses,
                monthlyTotalIncomes,
                expensesByCategory
        );
    }

    public String getUserId() {
        return userId;
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
        return expensesByCategory;
    }
}
