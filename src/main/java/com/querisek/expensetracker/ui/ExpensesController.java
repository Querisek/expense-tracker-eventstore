package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.expense.ExpenseCreatedEvent;
import com.querisek.expensetracker.domain.expense.ExpenseRepository;
import com.querisek.expensetracker.domain.income.IncomeRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
}
