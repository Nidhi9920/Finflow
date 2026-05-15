package com.nidhi.finflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;
    private String description;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

//    @PrePersist
//    public void setDate(){
//        this.date = LocalDate.now();
//    }
}
