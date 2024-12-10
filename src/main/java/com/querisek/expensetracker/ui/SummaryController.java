package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.expense.ExpenseCreatedEvent;
import com.querisek.expensetracker.domain.expense.ExpenseRepository;
import com.querisek.expensetracker.domain.income.IncomeCreatedEvent;
import com.querisek.expensetracker.domain.income.IncomeRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/summary")
public class SummaryController {
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public SummaryController(ExpenseRepository expenseRepository, IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    @GetMapping
    public String showSummaryToUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<ExpenseCreatedEvent> allExpenses = expenseRepository.listUsersExpensesByCategory(userDetails.getUsername(), "allCategories");
        List<IncomeCreatedEvent> allIncomes = incomeRepository.listUsersIncomes(userDetails.getUsername());

        double totalExpenses = allExpenses.stream()
                .mapToDouble(ExpenseCreatedEvent::getPrice)
                .sum();

        double totalIncomes = allIncomes.stream()
                .mapToDouble(IncomeCreatedEvent::getPrice)
                .sum();

        double totalFood = expenseRepository.getTotalPriceOfCategoryByDay("Jedzenie", allExpenses);
        double totalTravels = expenseRepository.getTotalPriceOfCategoryByDay("Podróże", allExpenses);
        double totalHealth = expenseRepository.getTotalPriceOfCategoryByDay("Zdrowie", allExpenses);
        double totalEntertainment = expenseRepository.getTotalPriceOfCategoryByDay("Rozrywka", allExpenses);
        double totalHome = expenseRepository.getTotalPriceOfCategoryByDay("Dom", allExpenses);
        double totalOthers = expenseRepository.getTotalPriceOfCategoryByDay("Inne", allExpenses);

        model.addAttribute("userEmail", userDetails.getUsername());
        model.addAttribute("allUsersExpenses", allExpenses);
        model.addAttribute("allUsersIncomes", allIncomes);
        model.addAttribute("totalUsersExpenses", totalExpenses);
        model.addAttribute("totalUsersIncome", totalIncomes);
        model.addAttribute("usersFood", totalFood);
        model.addAttribute("usersTravels", totalTravels);
        model.addAttribute("usersHealth", totalHealth);
        model.addAttribute("usersEntertainment", totalEntertainment);
        model.addAttribute("usersHome", totalHome);
        model.addAttribute("usersOthers", totalOthers);

        return "summary";
    }
}
