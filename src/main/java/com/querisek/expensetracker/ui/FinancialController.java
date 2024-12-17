package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.FinancialAccount;
import com.querisek.expensetracker.domain.common.*;
import com.querisek.expensetracker.domain.transaction.Transaction;
import com.querisek.expensetracker.domain.expense.AddExpenseRequest;
import com.querisek.expensetracker.domain.income.AddIncomeRequest;
import com.querisek.expensetracker.infrastructure.persistence.FinancialAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
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
                             HttpServletRequest httpRequest,
                             RedirectAttributes redirectAttributes) {
        Validation categoryValidation = Category.validate(request.getExpenseCategory());
        Validation descriptionValidation = Description.validate(request.getExpenseDescription());
        Validation moneyValidation = Money.validate(BigDecimal.valueOf(request.getPrice()));
        Validation dateValidation = Date.validate(request.getExpenseCreatedAt());
        if(!categoryValidation.isValid()) {
            redirectAttributes.addAttribute("categoryInvalid", categoryValidation.getMessage());
            return "redirect:" + httpRequest.getHeader("Referer");
        }
        if(!descriptionValidation.isValid()) {
            redirectAttributes.addAttribute("descriptionInvalid", descriptionValidation.getMessage());
            return "redirect:" + httpRequest.getHeader("Referer");
        }
        if(!moneyValidation.isValid()) {
            redirectAttributes.addAttribute("priceInvalid", moneyValidation.getMessage());
            return "redirect:" + httpRequest.getHeader("Referer");
        }
        if(!dateValidation.isValid()) {
            redirectAttributes.addAttribute("dateInvalid", dateValidation.getMessage());
            return "redirect:" + httpRequest.getHeader("Referer");
        }

        FinancialAccount financialAccount = financialAccountRepository.buildFinancialAccount(userDetails.getUsername());
        financialAccount.addExpense(
                request.getExpenseCategory(),
                request.getExpenseDescription(),
                request.getPrice(),
                request.getExpenseCreatedAt()
        );
        financialAccountRepository.save(financialAccount);
        if(httpRequest.getHeader("Referer") != null) {
            return "redirect:" + httpRequest.getHeader("Referer");
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/transactions/add/income")
    public String addIncome(@ModelAttribute AddIncomeRequest request,
                            @AuthenticationPrincipal UserDetails userDetails,
                            HttpServletRequest httpRequest,
                            RedirectAttributes redirectAttributes) {
        Validation descriptionValidation = Description.validate(request.getIncomeDescription());
        Validation moneyValidation = Money.validate(BigDecimal.valueOf(request.getPrice()));
        Validation dateValidation = Date.validate(request.getIncomeCreatedAt());
        if(!descriptionValidation.isValid()) {
            redirectAttributes.addAttribute("descriptionInvalid", descriptionValidation.getMessage());
            return "redirect:" + httpRequest.getHeader("Referer");
        }
        if(!moneyValidation.isValid()) {
            redirectAttributes.addAttribute("priceInvalid", moneyValidation.getMessage());
            return "redirect:" + httpRequest.getHeader("Referer");
        }
        if(!dateValidation.isValid()) {
            redirectAttributes.addAttribute("dateInvalid", dateValidation.getMessage());
            return "redirect:" + httpRequest.getHeader("Referer");
        }
        FinancialAccount financialAccount = financialAccountRepository.buildFinancialAccount(userDetails.getUsername());
        financialAccount.addIncome(
                request.getIncomeDescription(),
                request.getPrice(),
                request.getIncomeCreatedAt()
        );
        financialAccountRepository.save(financialAccount);
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
        financialAccountRepository.save(financialAccount);
        if(httpRequest.getHeader("Referer") != null) {
            return "redirect:" + httpRequest.getHeader("Referer");
        } else {
            return "redirect:/";
        }
    }
}
