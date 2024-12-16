package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.FinancialAccount;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.income.Income;
import com.querisek.expensetracker.domain.Transaction;
import com.querisek.expensetracker.infrastructure.persistence.FinancialAccountRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/transactions/summary")
public class SummaryController {
    private final FinancialAccountRepository financialAccountRepository;

    public SummaryController(FinancialAccountRepository financialAccountRepository) {
        this.financialAccountRepository = financialAccountRepository;
    }

    @GetMapping
    public String showSummaryToUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        FinancialAccount financialAccount = financialAccountRepository.buildFinancialAccount(userDetails.getUsername());
        List<Transaction> allTransactions = financialAccount.getTransactions();
        List<Transaction> allExpenses = allTransactions.stream()
                .filter(transaction -> transaction instanceof Expense)
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .toList();
        List<Transaction> allIncomes = allTransactions.stream()
                .filter(transaction -> transaction instanceof Income)
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .toList();
        double totalExpenses = allExpenses.stream()
                .mapToDouble(Transaction::getPrice)
                .sum();
        double totalIncomes = allIncomes.stream()
                .mapToDouble(Transaction::getPrice)
                .sum();
        Map<String, Double> expensesByCategory = allExpenses.stream()
                .map(transaction -> (Expense) transaction)
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingDouble(Transaction::getPrice)));

        model.addAttribute("userEmail", financialAccount.getUserId());
        model.addAttribute("allExpenses", allExpenses);
        model.addAttribute("allIncomes", allIncomes);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("totalIncomes", totalIncomes);
        model.addAttribute("expensesByCategory", expensesByCategory);

        return "summary";
    }
}
