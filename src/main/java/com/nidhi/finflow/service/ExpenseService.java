package com.nidhi.finflow.service;

import com.nidhi.finflow.dto.CreateExpenseRequest;
import com.nidhi.finflow.entity.Expense;
import com.nidhi.finflow.entity.User;
import com.nidhi.finflow.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
}
