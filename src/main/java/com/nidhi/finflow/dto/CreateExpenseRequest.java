package com.nidhi.finflow.dto;

import com.nidhi.finflow.entity.ExpenseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateExpenseRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotNull(message = "Amount is required")
    private BigDecimal amount;
    @NotNull(message = "Category is required")
    private ExpenseCategory category;
    private String description;
    private LocalDate date;
}
