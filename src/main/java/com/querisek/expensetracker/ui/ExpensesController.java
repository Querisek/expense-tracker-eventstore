package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.expense.AddExpenseRequest;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.expense.ExpenseCreatedEvent;
import com.querisek.expensetracker.domain.expense.ExpenseRepository;
import com.querisek.expensetracker.domain.income.IncomeRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/expenses")
public class ExpensesController {
    private final ExpenseRepository expenseRepository;

    public ExpensesController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping
    public String showExpenseCategoriesToUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<ExpenseCreatedEvent> allExpenses = expenseRepository.listUsersExpensesByCategory(userDetails.getUsername(), "allCategories");

        double totalExpenses = allExpenses.stream()
                .mapToDouble(ExpenseCreatedEvent::getPrice)
                .sum();

        double totalFood = expenseRepository.getTotalPriceOfCategoryByDay("Jedzenie", allExpenses);
        double totalTravels = expenseRepository.getTotalPriceOfCategoryByDay("Podróże", allExpenses);
        double totalHealth = expenseRepository.getTotalPriceOfCategoryByDay("Zdrowie", allExpenses);
        double totalEntertainment = expenseRepository.getTotalPriceOfCategoryByDay("Rozrywka", allExpenses);
        double totalHome = expenseRepository.getTotalPriceOfCategoryByDay("Dom", allExpenses);
        double totalOthers = expenseRepository.getTotalPriceOfCategoryByDay("Inne", allExpenses);

        model.addAttribute("userEmail", userDetails.getUsername());
        model.addAttribute("allExpenses", allExpenses);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("totalFood", totalFood);
        model.addAttribute("totalTravels", totalTravels);
        model.addAttribute("totalHealth", totalHealth);
        model.addAttribute("totalEntertainment", totalEntertainment);
        model.addAttribute("totalHome", totalHome);
        model.addAttribute("totalOthers", totalOthers);

        return "expenses";
    }

    @PostMapping
    public String addExpense(@ModelAttribute AddExpenseRequest expenseRequest, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest httpRequest) {
        Expense expense = new Expense(
                userDetails.getUsername(),
                expenseRequest.getExpenseCategory(),
                expenseRequest.getExpenseDescription(),
                expenseRequest.getPrice()
        );
        expenseRepository.addExpense(expense);
        if(httpRequest.getHeader("Referer") != null) {
            return "redirect:" + httpRequest.getHeader("Referer");
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/delete/{expenseId}")
    public String deleteExpense(@PathVariable UUID expenseId, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest httpRequest) {
        List<ExpenseCreatedEvent> allExpenses = expenseRepository.listUsersExpensesByCategory(userDetails.getUsername(), "allCategories");
        ExpenseCreatedEvent expenseToDelete = allExpenses.stream()
                .filter(e -> e.getExpenseId().equals(expenseId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Wybrany wydatek nie istnieje"));
        Expense expense = new Expense(
                expenseToDelete.getExpenseId(),
                expenseToDelete.getUserId(),
                expenseToDelete.getExpenseCategory(),
                expenseToDelete.getExpenseDescription(),
                expenseToDelete.getPrice()
        );
        expenseRepository.deleteExpense(expense);
        if(httpRequest.getHeader("Referer") != null) {
            return "redirect:" + httpRequest.getHeader("Referer");
        } else {
            return "redirect:/";
        }
    }
}
