package com.querisek.expensetracker.ui;

import com.google.common.collect.ImmutableList;
import com.querisek.expensetracker.domain.FinancialAccount;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.transaction.Transaction;
import com.querisek.expensetracker.infrastructure.persistence.FinancialAccountRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.Comparator;

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

        model.addAttribute("currentMonth", yearMonth);
        model.addAttribute("userEmail", financialAccount.getUserId());
        model.addAttribute("allExpenses", financialAccount.getTransactions().stream()
                .filter(transaction -> transaction instanceof Expense)
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .collect(ImmutableList.toImmutableList()));
        model.addAttribute("currentMonthExpenses", financialAccount.getCurrentMonthExpenses());
        model.addAttribute("currentMonthExpensesByCategory", financialAccount.getCurrentMonthExpensesByCategory());

        return "expenses";
    }
}
