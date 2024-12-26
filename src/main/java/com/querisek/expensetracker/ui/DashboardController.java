package com.querisek.expensetracker.ui;

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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Controller
public class DashboardController {
    private final FinancialAccountRepository financialAccountRepository;

    public DashboardController(FinancialAccountRepository financialAccountRepository) {
        this.financialAccountRepository = financialAccountRepository;
    }

    @GetMapping("/")
    public String showDashboard(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                @RequestParam(required = false) Integer year,
                                @RequestParam(required = false) Integer month,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        YearMonth yearMonth;
        if(year != null && month != null) {
            yearMonth = YearMonth.of(year, month);
        } else {
            yearMonth = YearMonth.now();
        }
        FinancialAccount financialAccount = financialAccountRepository.buildFinancialAccount(userDetails.getUsername(), yearMonth);
        LocalDate selectedDate = Objects.requireNonNullElseGet(date, LocalDate::now);
//        List<Transaction> allTransactions = financialAccount.getTransactions();
//        List<Transaction> allTransactionsFilteredByDay = allTransactions.stream()
//                .filter(transaction -> transaction.getCreatedAt().equals(selectedDate))
//                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
//                .toList();
//        List<Transaction> expensesFilteredByDay = allTransactionsFilteredByDay.stream()
//                .filter(transaction -> transaction instanceof Expense)
//                .toList();
//        List<Transaction> incomesFilteredByDay = allTransactionsFilteredByDay.stream()
//                .filter(transaction -> transaction instanceof Income)
//                .toList();
//        double totalExpensesFilteredByDay = expensesFilteredByDay.stream()
//                .mapToDouble(Transaction::getPrice)
//                .sum();
//        double totalIncomeFilteredByDay = incomesFilteredByDay.stream()
//                .mapToDouble(Transaction::getPrice)
//                .sum();
//        Map<String, Double> expensesByCategory = expensesFilteredByDay.stream()
//                .map(transaction -> (Expense) transaction)
//                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Transaction::getPrice)));
//
//        model.addAttribute("selectedDate", selectedDate);
//        model.addAttribute("userEmail", financialAccount.getUserId());
//        model.addAttribute("transactions", allTransactionsFilteredByDay);
//        model.addAttribute("expensesByDay", expensesFilteredByDay);
//        model.addAttribute("incomesByDay", incomesFilteredByDay);
//        model.addAttribute("totalExpenses", totalExpensesFilteredByDay);
//        model.addAttribute("totalIncome", totalIncomeFilteredByDay);
//        model.addAttribute("expensesByCategory", expensesByCategory);
        List<Transaction> allTransactionsFilteredByDay = financialAccount.getTransactions().stream()
                .filter(transaction -> transaction.getCreatedAt().equals(selectedDate))
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .toList();

        List<Transaction> expensesFilteredByDay = allTransactionsFilteredByDay.stream()
                .filter(transaction -> transaction instanceof Expense)
                .toList();

        List<Transaction> incomesFilteredByDay = allTransactionsFilteredByDay.stream()
                .filter(transaction -> transaction instanceof Income)
                .toList();

        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("currentMonth", yearMonth);
        model.addAttribute("userEmail", financialAccount.getUserId());
        model.addAttribute("transactions", allTransactionsFilteredByDay);
        model.addAttribute("expensesByDay", expensesFilteredByDay);
        model.addAttribute("incomesByDay", incomesFilteredByDay);
        model.addAttribute("totalExpenses", financialAccount.getTotalExpenses());
        model.addAttribute("totalIncome", financialAccount.getTotalIncomes());
        model.addAttribute("currentMonthExpenses", financialAccount.getCurrentMonthExpenses());
        model.addAttribute("currentMonthIncome", financialAccount.getCurrentMonthIncomes());
        model.addAttribute("expensesByCategory", financialAccount.getTotalExpensesByCategory());
        model.addAttribute("currentMonthExpensesByCategory", financialAccount.getCurrentMonthExpensesByCategory());

        return "dashboard";
    }
}
