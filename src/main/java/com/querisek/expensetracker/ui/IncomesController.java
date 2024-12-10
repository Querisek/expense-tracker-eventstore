package com.querisek.expensetracker.ui;

import com.querisek.expensetracker.domain.expense.ExpenseRepository;
import com.querisek.expensetracker.domain.income.IncomeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/incomes")
public class IncomesController {
    private final IncomeRepository incomeRepository;

    public IncomesController(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @GetMapping
    public String showAllIncomesToUser() {
        return "incomes";
    }
}
