package com.nidhi.finflow.dto;

import com.nidhi.finflow.entity.ExpenseCategory;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ExpenseResponse {
    private Long id;
    private String title;
    private BigDecimal amount;
    private ExpenseCategory category;
    private String description;
    private LocalDate date;
}
