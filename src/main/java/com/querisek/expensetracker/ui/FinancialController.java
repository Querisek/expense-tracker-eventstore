package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.FinancialAccount;
import com.querisek.expensetracker.domain.Transaction;
import com.querisek.expensetracker.domain.expense.AddExpenseRequest;
import com.querisek.expensetracker.domain.income.AddIncomeRequest;
import com.querisek.expensetracker.infrastructure.persistence.FinancialAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class FinancialController {
    private final FinancialAccountRepository financialAccountRepository;

    public FinancialController(FinancialAccountRepository financialAccountRepository) {
        this.financialAccountRepository = financialAccountRepository;
    }

    @PostMapping("/transactions/add/expense")
    public String addExpense(@ModelAttribute AddExpenseRequest request,
                             @AuthenticationPrincipal UserDetails userDetails,
                             HttpServletRequest httpRequest) {
        FinancialAccount financialAccount = financialAccountRepository.buildFinancialAccount(userDetails.getUsername());
        Transaction expense = financialAccount.addExpense(
                request.getExpenseCategory(),
                request.getExpenseDescription(),
                request.getPrice(),
                request.getExpenseCreatedAt()
        );
        financialAccountRepository.addTransaction(financialAccount, expense);
        if(httpRequest.getHeader("Referer") != null) {
            return "redirect:" + httpRequest.getHeader("Referer");
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/transactions/add/income")
    public String addIncome(@ModelAttribute AddIncomeRequest request,
                            @AuthenticationPrincipal UserDetails userDetails,
                            HttpServletRequest httpRequest) {
        FinancialAccount financialAccount = financialAccountRepository.buildFinancialAccount(userDetails.getUsername());
        Transaction income = financialAccount.addIncome(
                request.getIncomeDescription(),
                request.getPrice(),
                request.getIncomeCreatedAt()
        );
        financialAccountRepository.addTransaction(financialAccount, income);
        if(httpRequest.getHeader("Referer") != null) {
            return "redirect:" + httpRequest.getHeader("Referer");
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/transactions/delete/{id}")
    public String deleteTransaction(@PathVariable UUID id,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    HttpServletRequest httpRequest) {
        FinancialAccount financialAccount = financialAccountRepository.buildFinancialAccount(userDetails.getUsername());
        financialAccount.removeTransaction(id);
        financialAccountRepository.removeTransaction(financialAccount, id);
        if(httpRequest.getHeader("Referer") != null) {
            return "redirect:" + httpRequest.getHeader("Referer");
        } else {
            return "redirect:/";
        }
    }
}
