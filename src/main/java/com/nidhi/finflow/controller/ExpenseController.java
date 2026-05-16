package com.nidhi.finflow.controller;

import com.nidhi.finflow.dto.CreateExpenseRequest;
import com.nidhi.finflow.dto.UpdateExpenseRequest;
import com.nidhi.finflow.entity.Expense;
import com.nidhi.finflow.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    @PostMapping
    public Expense createExpense(@Valid @RequestBody CreateExpenseRequest createExpenseRequest){
        return expenseService.createExpense(createExpenseRequest);
    }
    @GetMapping("/my")
    public List<Expense> getMyExpense(){
        return expenseService.getMyExpenses();
    }
    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id, @RequestBody UpdateExpenseRequest updateExpenseRequest){
        return expenseService.updateExpense(id,updateExpenseRequest);
    }
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
        return "Expense deleted successfully";
    }
}
