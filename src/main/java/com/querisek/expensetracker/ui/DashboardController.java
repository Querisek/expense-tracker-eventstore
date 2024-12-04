package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.AddExpenseRequest;
import com.querisek.expensetracker.domain.Expense;
import com.querisek.expensetracker.domain.ExpenseCreatedEvent;
import com.querisek.expensetracker.domain.ExpenseRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private final ExpenseRepository expenseRepository;

    public DashboardController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/")
    public String showDashboardToUser(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        LocalDate selectedDate = Objects.requireNonNullElseGet(date, LocalDate::now);
        LocalDateTime startOfTheDay = selectedDate.atStartOfDay();
        LocalDateTime endOfTheDay = selectedDate.atTime(23, 59, 59);
        List<ExpenseCreatedEvent> filteredExpensesByDate = expenseRepository.listUsersExpenses(userDetails.getUsername()).stream()
                .filter(expense -> {
                    LocalDateTime expenseDate = expense.getExpenseCreatedAt();
                    return !expenseDate.isBefore(startOfTheDay) && !expenseDate.isAfter(endOfTheDay);
                })
                .collect(Collectors.toList());

        double totalExpenses = filteredExpensesByDate.stream()
                .mapToDouble(ExpenseCreatedEvent::getPrice)
                .sum();

        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("userEmail", userDetails.getUsername());
        model.addAttribute("addExpenseRequest", new AddExpenseRequest());
        model.addAttribute("allUsersExpenses", filteredExpensesByDate);
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
