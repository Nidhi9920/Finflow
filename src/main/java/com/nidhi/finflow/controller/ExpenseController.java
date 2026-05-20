package com.nidhi.finflow.controller;

import com.nidhi.finflow.dto.CreateExpenseRequest;
import com.nidhi.finflow.dto.ExpenseResponse;
import com.nidhi.finflow.dto.UpdateExpenseRequest;
import com.nidhi.finflow.entity.ExpenseCategory;
import com.nidhi.finflow.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "Expense APIs")
@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    @Operation(summary = "Create expense", description = "Create a new expense for the logged-in user")
    @PostMapping
    public ExpenseResponse createExpense(@Valid @RequestBody CreateExpenseRequest createExpenseRequest){
        return expenseService.createExpense(createExpenseRequest);
    }
    @Operation(summary = "Get my expenses", description = "Retrieve all expenses of currently authenticated user")
    @GetMapping("/my")
    public Page<ExpenseResponse> getMyExpense(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size,
                                              @RequestParam(defaultValue = "date") String sortBy,
                                              @RequestParam(defaultValue = "desc") String direction){
        return expenseService.getMyExpenses(page, size, sortBy, direction);
    }
    @Operation(summary = "Update expense", description = "Update an expense by ID. Only owner can update")
    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(@PathVariable Long id, @RequestBody UpdateExpenseRequest updateExpenseRequest){
        return expenseService.updateExpense(id,updateExpenseRequest);
    }
    @Operation(summary = "Delete expense", description = "Delete an expense by ID. Only owner can delete")
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
        return "Expense deleted successfully";
    }
    @Operation(summary = "Get total spending", description = "Calculate total expense amount of logged-in user")
    @GetMapping("/analytics/total")
    public Map<String, BigDecimal> getTotalSpending(){
        BigDecimal total = expenseService.getTotalSpending();
        return Map.of("totalSpending", total);
    }
    @Operation(summary = "Get category-wise spending", description = "Retrieve total spending grouped by expense category")
    @GetMapping("/analytics/category")
    public Map<String, BigDecimal> getCategoryWiseSpending(){
        return expenseService.getCategoryWiseSpending();
    }
    @Operation(summary = "Get monthly spending summary", description = "Retrieve monthly spending summary of logged-in user")
    @GetMapping("/analytics/monthly")
    public Map<String, BigDecimal> getMonthlySpending(){
        return expenseService.getMonthlySpending();
    }
    @Operation(summary = "Filter expense", description = "Filter expenses by category and date range")
    @GetMapping("/filter")
    public List<ExpenseResponse> getFilterExpenses(@RequestParam(required = false)ExpenseCategory category,
                                                  @RequestParam(required = false)LocalDate startDate,
                                                  @RequestParam(required = false) LocalDate endDate,
                                                  @RequestParam(required = false) BigDecimal minAmount,
                                                  @RequestParam(required = false) BigDecimal maxAmount,
                                                  @RequestParam(required = false) String keyword){
        return expenseService.filterExpenses(category, startDate, endDate, minAmount, maxAmount, keyword);
    }
}
