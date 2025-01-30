package com.querisek.expensetracker.ui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.querisek.expensetracker.domain.FinancialAccount;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.income.Income;
import com.querisek.expensetracker.domain.transaction.Transaction;
import com.querisek.expensetracker.infrastructure.persistence.FinancialAccountRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.Objects;


@Controller
public class DashboardController {
    private final FinancialAccountRepository financialAccountRepository;

    public DashboardController(FinancialAccountRepository financialAccountRepository) {
        this.financialAccountRepository = financialAccountRepository;
    }

    @GetMapping("/")
    public String showDashboard(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        YearMonth yearMonth;
        if(date != null) {
            yearMonth = YearMonth.from(date);
        } else {
            yearMonth = YearMonth.now();
        }
        if(yearMonth.equals(YearMonth.now())) {
            financialAccountRepository.tryToSnapshot(userDetails.getUsername(), yearMonth);
        }
        FinancialAccount financialAccount = financialAccountRepository.buildFinancialAccount(userDetails.getUsername(), yearMonth);
        LocalDate selectedDate = Objects.requireNonNullElseGet(date, LocalDate::now);
        ImmutableList<Transaction> allTransactionsFilteredByDay = financialAccount.getTransactions().stream()
                .filter(transaction -> transaction.getCreatedAt().equals(selectedDate))
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .collect(ImmutableList.toImmutableList());
        ImmutableList<Transaction> expensesFilteredByDay = allTransactionsFilteredByDay.stream()
                .filter(transaction -> transaction instanceof Expense)
                .collect(ImmutableList.toImmutableList());
        ImmutableList<Transaction> incomeFilteredByDay = allTransactionsFilteredByDay.stream()
                .filter(transaction -> transaction instanceof Income)
                .collect(ImmutableList.toImmutableList());
        double totalExpensesFilteredByDay = expensesFilteredByDay.stream()
                .mapToDouble(Transaction::getPrice)
                .sum();
        double totalIncomeFilteredByDay = incomeFilteredByDay.stream()
                .mapToDouble(Transaction::getPrice)
                .sum();
        ImmutableMap<String, Double> expensesByCategory = expensesFilteredByDay.stream()
                .map(transaction -> (Expense) transaction)
                .collect(ImmutableMap.toImmutableMap(Expense::getCategory, Transaction::getPrice, Double::sum));

        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("currentMonth", yearMonth);
        model.addAttribute("userEmail", financialAccount.getUserEmail());
        model.addAttribute("transactions", allTransactionsFilteredByDay);
        model.addAttribute("expensesByDay", expensesFilteredByDay);
        model.addAttribute("incomeByDay", incomeFilteredByDay);
        model.addAttribute("totalExpenses", totalExpensesFilteredByDay);
        model.addAttribute("totalIncome", totalIncomeFilteredByDay);
        model.addAttribute("expensesByCategory", expensesByCategory);

        return "dashboard";
    }
}
