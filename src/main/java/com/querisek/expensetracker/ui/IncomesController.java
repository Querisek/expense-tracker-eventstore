package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.expense.ExpenseRepository;
import com.querisek.expensetracker.domain.income.AddIncomeRequest;
import com.querisek.expensetracker.domain.income.Income;
import com.querisek.expensetracker.domain.income.IncomeCreatedEvent;
import com.querisek.expensetracker.domain.income.IncomeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/incomes")
public class IncomesController {
    private final IncomeRepository incomeRepository;

    public IncomesController(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @PostMapping
    public String addIncome(@ModelAttribute AddIncomeRequest incomeRequest, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest httpRequest) {
        Income income = new Income(
                userDetails.getUsername(),
                incomeRequest.getIncomeDescription(),
                incomeRequest.getPrice()
        );
        incomeRepository.addIncome(income);
        if(httpRequest.getHeader("Referer") != null) {
            return "redirect:" + httpRequest.getHeader("Referer");
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/delete/{incomeId}")
    public String deleteIncome(@PathVariable UUID incomeId, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest httpRequest) {
        List<IncomeCreatedEvent> allIncomes = incomeRepository.listUsersIncomes(userDetails.getUsername());
        IncomeCreatedEvent incomeToDelete = allIncomes.stream()
                .filter(e -> e.getIncomeId().equals(incomeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Wybrany wydatek nie istnieje"));
        Income income = new Income(
                incomeToDelete.getIncomeId(),
                incomeToDelete.getUserId(),
                incomeToDelete.getIncomeDescription(),
                incomeToDelete.getPrice()
        );
        incomeRepository.deleteIncome(income);
        if(httpRequest.getHeader("Referer") != null) {
            return "redirect:" + httpRequest.getHeader("Referer");
        } else {
            return "redirect:/";
        }
    }
}
