package com.nidhi.finflow;

import com.nidhi.finflow.dto.CreateExpenseRequest;
import com.nidhi.finflow.dto.ExpenseResponse;
import com.nidhi.finflow.dto.UpdateExpenseRequest;
import com.nidhi.finflow.entity.Expense;
import com.nidhi.finflow.entity.ExpenseCategory;
import com.nidhi.finflow.entity.Role;
import com.nidhi.finflow.entity.User;
import com.nidhi.finflow.exception.AccessDeniedException;
import com.nidhi.finflow.repository.ExpenseRepository;
import com.nidhi.finflow.service.ExpenseService;
import com.nidhi.finflow.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private ExpenseService expenseService;
    @Test
    void shouldCreateExpenseSuccessfully(){
        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setTitle("Zomato");
        request.setAmount(BigDecimal.valueOf(450));
        request.setCategory(ExpenseCategory.FOOD);
        request.setDescription("Dinner");
        User currentUser = User.builder()
                .id(1L)
                .name("Nidhi")
                .email("nidhi@gmail.com")
                .role(Role.USER)
                .build();
        Expense savedExpense = Expense.builder()
                .id(1L)
                .title("Zomato")
                .amount(BigDecimal.valueOf(450))
                .category(ExpenseCategory.FOOD)
                .description("Dinner")
                .date(LocalDate.now())
                .user(currentUser)
                .build();
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);
        ExpenseResponse response = expenseService.createExpense(request);
        assertNotNull(response);
        assertEquals("Zomato", response.getTitle());
        assertEquals(ExpenseCategory.FOOD, response.getCategory());
        verify(expenseRepository).save(any(Expense.class));
    }
    @Test
    void shouldThrowExceptionWhenUserIsNotOwner(){
        User expenseOwner = User.builder()
                .id(1L)
                .email("owner@gmail.com")
                .role(Role.USER)
                .build();
        User currentUser = User.builder()
                .id(2L)
                .email("other@gmail.com")
                .role(Role.USER)
                .build();
        Expense expense = Expense.builder()
                .id(1L)
                .title("Zomato")
                .user(expenseOwner)
                .build();
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        UpdateExpenseRequest request = new UpdateExpenseRequest();
        request.setTitle("Updated title");
        assertThrows(AccessDeniedException.class, ()-> expenseService.updateExpense(1L, request));
        verify(expenseRepository, never()).save(any(Expense.class));
    }
    @Test
    void shouldUpdateExpenseSuccessfully(){
        User currentUser = User.builder()
                .id(1L)
                .email("nidhi@gmail.com")
                .role(Role.USER)
                .build();
        Expense expense = Expense.builder()
                .id(1L)
                .title("Zomato")
                .amount(BigDecimal.valueOf(450))
                .user(currentUser)
                .build();
        UpdateExpenseRequest request = new UpdateExpenseRequest();
        request.setTitle("Uber Eats");
        request.setAmount(BigDecimal.valueOf(600));
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        ExpenseResponse response = expenseService.updateExpense(1L, request);
        assertNotNull(response);
        assertEquals("Uber Eats", response.getTitle());
        assertEquals(BigDecimal.valueOf(600), response.getAmount());
        verify(expenseRepository).save(any(Expense.class));
    }
    @Test
    void shouldDeleteExpenseWhenUserIsOwner(){
        User currentUser = User.builder()
                .id(1L)
                .role(Role.USER)
                .build();
        Expense expense = Expense.builder()
                .id(1L)
                .user(currentUser)
                .build();
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        expenseService.deleteExpense(1L);
        verify(expenseRepository).delete(expense);
    }
    @Test
    void shouldDeleteExpenseWhenUserIsAdmin(){
        User owner = User.builder()
                .id(1L)
                .role(Role.USER)
                .build();
        User admin = User.builder()
                .id(2L)
                .role(Role.ADMIN)
                .build();
        Expense expense = Expense.builder()
                .id(1L)
                .user(owner)
                .build();
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(userService.getCurrentUser()).thenReturn(admin);
        expenseService.deleteExpense(1L);
        verify(expenseRepository).delete(expense);
    }
    @Test
    void shouldThrowExceptionWhenUserIsNotOwnerOrAdmin(){
        User owner = User.builder()
                .id(1L)
                .role(Role.USER)
                .build();
        User otherUser = User.builder()
                .id(2L)
                .role(Role.USER)
                .build();
        Expense expense = Expense.builder()
                .id(1L)
                .user(owner)
                .build();
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));
        when(userService.getCurrentUser()).thenReturn(otherUser);
        assertThrows(AccessDeniedException.class, ()->expenseService.deleteExpense(1L));
        verify(expenseRepository, never()).delete(any(Expense.class));
    }
    @Test
    void shouldReturnTotalSpending(){
        User currentUser = User.builder()
                .id(1L)
                .build();
        when(userService.getCurrentUser()).thenReturn(currentUser);
        when(expenseRepository.getTotalSpendingByUserId(1L)).thenReturn(BigDecimal.valueOf(2500));
        BigDecimal total = expenseService.getTotalSpending();
        assertEquals(BigDecimal.valueOf(2500), total);
    }
}
