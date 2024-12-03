package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.AddExpenseRequest;
import com.querisek.expensetracker.domain.Expense;
import com.querisek.expensetracker.domain.ExpenseCreatedEvent;
import com.querisek.expensetracker.domain.ExpenseRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DashboardController {
    private final ExpenseRepository expenseRepository;

    public DashboardController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/")
    public String showDashboardToUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("userEmail", userDetails.getUsername());
        model.addAttribute("addExpenseRequest", new AddExpenseRequest());
        model.addAttribute("allUsersExpenses", expenseRepository.listUsersExpenses(userDetails.getUsername()));
        double totalExpenses = expenseRepository.listUsersExpenses(userDetails.getUsername()).stream()
                .mapToDouble(ExpenseCreatedEvent::getPrice)
                .sum();
        model.addAttribute("totalUsersExpenses", totalExpenses);
        return "dashboard";
    }

    @PostMapping("/expenses")
    public String addExpense(@ModelAttribute AddExpenseRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        Expense expense = new Expense(
                userDetails.getUsername(),
                request.getExpenseCategory(),
                request.getExpenseDescription(),
                request.getPrice()
        );
        expenseRepository.addExpense(expense);
        return "redirect:/?success";
    }
}
