package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.expense.AddExpenseRequest;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.expense.ExpenseCreatedEvent;
import com.querisek.expensetracker.domain.expense.ExpenseRepository;
import com.querisek.expensetracker.domain.income.AddIncomeRequest;
import com.querisek.expensetracker.domain.income.Income;
import com.querisek.expensetracker.domain.income.IncomeCreatedEvent;
import com.querisek.expensetracker.domain.income.IncomeRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class DashboardController {
    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public DashboardController(ExpenseRepository expenseRepository, IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    @GetMapping("/")
    public String showDashboardToUser(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                      @RequestParam(required = false, defaultValue = "expenses") String type,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      Model model) {
        LocalDate selectedDate = Objects.requireNonNullElseGet(date, LocalDate::now);
        LocalDateTime startOfTheDay = selectedDate.atStartOfDay();
        LocalDateTime endOfTheDay = selectedDate.atTime(23, 59, 59);
        List<ExpenseCreatedEvent> filteredAllExpensesByDate = expenseRepository.listUsersExpensesByCategory(userDetails.getUsername(), "allCategories").stream()
                .filter(expense -> expense.getExpenseCreatedAt().equals(selectedDate))
                .collect(Collectors.toList());

        double totalPriceOfUsersExpenses = filteredAllExpensesByDate.stream()
                .mapToDouble(ExpenseCreatedEvent::getPrice)
                .sum();

        List<IncomeCreatedEvent> filteredAllIncomesByDate = incomeRepository.listUsersIncomes(userDetails.getUsername()).stream()
                .filter(income -> income.getIncomeCreatedAt().equals(selectedDate))
                .toList();

        double totalWorthOfUsersDailyIncomes = filteredAllIncomesByDate.stream()
                .mapToDouble(IncomeCreatedEvent::getPrice)
                .sum();

        double totalPriceOfUsersFoodFilteredByDay = expenseRepository.getTotalPriceOfCategoryByDay("Jedzenie", filteredAllExpensesByDate);
        double totalPriceOfUsersTravelsFilteredByDay = expenseRepository.getTotalPriceOfCategoryByDay("Podróże", filteredAllExpensesByDate);
        double totalPriceOfUsersHealthFilteredByDay = expenseRepository.getTotalPriceOfCategoryByDay("Zdrowie", filteredAllExpensesByDate);
        double totalPriceOfUsersEntertainmentFilteredByDay = expenseRepository.getTotalPriceOfCategoryByDay("Rozrywka", filteredAllExpensesByDate);
        double totalPriceOfUsersHomeFilteredByDay = expenseRepository.getTotalPriceOfCategoryByDay("Dom", filteredAllExpensesByDate);
        double totalPriceOfUsersOthersFilteredByDay = expenseRepository.getTotalPriceOfCategoryByDay("Inne", filteredAllExpensesByDate);

        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("userEmail", userDetails.getUsername());
        model.addAttribute("addExpenseRequest", new AddExpenseRequest());
        model.addAttribute("allUsersExpenses", filteredAllExpensesByDate);
        model.addAttribute("totalUsersExpenses", totalPriceOfUsersExpenses);
        model.addAttribute("usersFoodByDay", totalPriceOfUsersFoodFilteredByDay);
        model.addAttribute("usersTravelsByDay", totalPriceOfUsersTravelsFilteredByDay);
        model.addAttribute("usersHealthByDay", totalPriceOfUsersHealthFilteredByDay);
        model.addAttribute("usersEntertainmentByDay", totalPriceOfUsersEntertainmentFilteredByDay);
        model.addAttribute("usersHomeByDay", totalPriceOfUsersHomeFilteredByDay);
        model.addAttribute("usersOthersByDay", totalPriceOfUsersOthersFilteredByDay);
        model.addAttribute("addIncomeRequest", new AddIncomeRequest());
        model.addAttribute("allUsersDailyIncomes", filteredAllIncomesByDate);
        model.addAttribute("totalUsersDailyIncome", totalWorthOfUsersDailyIncomes);
        return "dashboard";
    }
}
