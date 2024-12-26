package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.FinancialAccount;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.transaction.Transaction;
import com.querisek.expensetracker.infrastructure.persistence.FinancialAccountRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transactions/expenses")
public class ExpensesCategoryController {
    private final FinancialAccountRepository financialAccountRepository;

    public ExpensesCategoryController(FinancialAccountRepository financialAccountRepository) {
        this.financialAccountRepository = financialAccountRepository;
    }

    @GetMapping
    public String showExpenseCategoriesToUser(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam(required = false) Integer year,
                                              @RequestParam(required = false) Integer month,
                                              Model model) {
        YearMonth yearMonth;
        if(year != null && month != null) {
            yearMonth = YearMonth.of(year, month);
        } else {
            yearMonth = YearMonth.now();
        }
        FinancialAccount financialAccount = financialAccountRepository.buildFinancialAccount(userDetails.getUsername(), yearMonth);
//        List<Transaction> allTransactions = financialAccount.getTransactions();
//        List<Transaction> allExpenses = allTransactions.stream()
//                .filter(transaction -> transaction instanceof Expense)
//                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
//                .toList();
//        double totalExpenses = allExpenses.stream()
//                .mapToDouble(Transaction::getPrice)
//                .sum();
//        Map<String, Double> expensesByCategory = allExpenses.stream()
//                .map(transaction -> (Expense) transaction)
//                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Transaction::getPrice)));
//
//        model.addAttribute("userEmail", financialAccount.getUserId());
//        model.addAttribute("allExpenses", allExpenses);
//        model.addAttribute("totalExpenses", totalExpenses);
//        model.addAttribute("expensesByCategory", expensesByCategory);
        model.addAttribute("currentMonth", yearMonth);
        model.addAttribute("userEmail", financialAccount.getUserId());
        model.addAttribute("allExpenses", financialAccount.getTransactions().stream()
                .filter(t -> t instanceof Expense)
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .toList());
        model.addAttribute("totalExpenses", financialAccount.getTotalExpenses());
        model.addAttribute("currentMonthExpenses", financialAccount.getCurrentMonthExpenses());
        model.addAttribute("expensesByCategory", financialAccount.getTotalExpensesByCategory());
        model.addAttribute("currentMonthExpensesByCategory", financialAccount.getCurrentMonthExpensesByCategory());

        return "expenses";
    }
}
