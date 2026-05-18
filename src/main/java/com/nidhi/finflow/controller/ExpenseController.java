package com.nidhi.finflow.controller;

import com.nidhi.finflow.dto.CreateExpenseRequest;
import com.nidhi.finflow.dto.UpdateExpenseRequest;
import com.nidhi.finflow.entity.Expense;
import com.nidhi.finflow.entity.ExpenseCategory;
import com.nidhi.finflow.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/analytics/total")
    public Map<String, BigDecimal> getTotalSpending(){
        BigDecimal total = expenseService.getTotalSpending();
        return Map.of("totalSpending", total);
    }
    @GetMapping("/analytics/category")
    public Map<String, BigDecimal> getCategoryWiseSpending(){
        return expenseService.getCategoryWiseSpending();
    }
    @GetMapping("/analytics/monthly")
    public Map<String, BigDecimal> getMonthlySpending(){
        return expenseService.getMonthlySpending();
    }
    @GetMapping("/filter")
    public List<Expense> getExpenseFilter(@RequestParam(required = false)ExpenseCategory category,
                                          @RequestParam(required = false)LocalDate startDate,
                                          @RequestParam(required = false) LocalDate endDate){
        return expenseService.filterExpense(category, startDate, endDate);
    }
}
