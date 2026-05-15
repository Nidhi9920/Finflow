package com.nidhi.finflow.controller;

import com.nidhi.finflow.dto.CreateExpenseRequest;
import com.nidhi.finflow.entity.Expense;
import com.nidhi.finflow.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    @PostMapping
    public Expense createExpense(CreateExpenseRequest createExpenseRequest){
        return expenseService.createExpense(createExpenseRequest);
    }
    @GetMapping("/my")
    public List<Expense> getMyExpense(){
        return expenseService.getMyExpenses();
    }
}
