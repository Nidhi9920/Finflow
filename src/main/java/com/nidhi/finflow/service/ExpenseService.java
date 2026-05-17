package com.nidhi.finflow.service;

import com.nidhi.finflow.dto.CreateExpenseRequest;
import com.nidhi.finflow.dto.UpdateExpenseRequest;
import com.nidhi.finflow.entity.Expense;
import com.nidhi.finflow.entity.ExpenseCategory;
import com.nidhi.finflow.entity.User;
import com.nidhi.finflow.exception.ResourceNotFoundException;
import com.nidhi.finflow.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    public Expense createExpense(CreateExpenseRequest createExpenseRequest){
        Expense expense = Expense.builder()
                .title(createExpenseRequest.getTitle())
                .amount(createExpenseRequest.getAmount())
                .category(createExpenseRequest.getCategory())
                .description(createExpenseRequest.getDescription())
                .date(Optional.ofNullable(createExpenseRequest.getDate()).orElse(LocalDate.now()))
                .build();
        User user = userService.getCurrentUser();
        expense.setUser(user);
        return expenseRepository.save(expense);
    }
    public List<Expense> getMyExpenses(){
        User user = userService.getCurrentUser();
        return expenseRepository.findByUserId(user.getId());
    }
    public List<Expense> getAllExpenses(){
        return expenseRepository.findAll();
    }
    public Expense updateExpense(Long id, UpdateExpenseRequest request){
        Expense expense = expenseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Expense not found"));
        User currentUser = userService.getCurrentUser();
        if(!expense.getUser().getId().equals(currentUser.getId())){
            throw new ResourceNotFoundException("Access denied");
        }
        if(request.getTitle() != null){
            expense.setTitle(request.getTitle());
        }
        if(request.getAmount() != null){
            expense.setAmount(request.getAmount());
        }
        if (request.getCategory() != null){
            expense.setCategory(request.getCategory());
        }
        if(request.getDescription() != null){
            expense.setDescription(request.getDescription());
        }
        if(request.getDate() != null){
            expense.setDate(request.getDate());
        }
        return expenseRepository.save(expense);
    }
    public void deleteExpense(Long id){
        Expense expense = expenseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Expense not found"));
        User currentUser = userService.getCurrentUser();
        boolean isOwner = expense.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        if(!isOwner && !isAdmin){
            throw new ResourceNotFoundException("Access denied");
        }
        expenseRepository.delete(expense);
    }
    public BigDecimal getTotalSpending(){
        User user = userService.getCurrentUser();
        return expenseRepository.getTotalSpendingByUserId(user.getId());
    }
    public Map<String, BigDecimal> getCategoryWiseSpending(){
        User user = userService.getCurrentUser();
        List<Object[]> results = expenseRepository.getCategoryWiseSpending(user.getId());
        Map<String, BigDecimal> spending = new HashMap<>();
        for(Object[] row : results){
            String category = row[0].toString();
            BigDecimal total = (BigDecimal) row[1];
            spending.put(category,total);
        }
        return spending;
    }
    public Map<String, BigDecimal> getMonthlySpending(){
        User user = userService.getCurrentUser();
        List<Object[]> results = expenseRepository.getMonthlySpending(user.getId());
        Map<String, BigDecimal> monthlySpending = new LinkedHashMap<>();
        for(Object[] row : results){
            Integer monthNumber = (Integer) row[0];
            BigDecimal total = (BigDecimal) row[1];
            String monthName = Month.of(monthNumber).name();
            monthlySpending.put(monthName, total);
        }
        return monthlySpending;
    }
    public List<Expense> getExpenseFilter(ExpenseCategory category, LocalDate startDate, LocalDate endDate){
        User user = userService.getCurrentUser();
        return expenseRepository.filterExpense(user.getId(), category, startDate, endDate);
    }
}
