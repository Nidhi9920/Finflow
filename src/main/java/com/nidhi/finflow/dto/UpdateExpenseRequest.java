package com.nidhi.finflow.dto;

import com.nidhi.finflow.entity.ExpenseCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateExpenseRequest {
    private String title;
    private BigDecimal amount;
    private ExpenseCategory category;
    private String description;
    private LocalDate date;
}
