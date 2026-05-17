package com.nidhi.finflow.repository;

import com.nidhi.finflow.entity.Expense;
import com.nidhi.finflow.entity.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserId(Long userId);
    @Query("""
            SELECT COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE e.user.id = :userId
            """)
    BigDecimal getTotalSpendingByUserId(@Param("userId") Long userId);
    @Query("""
            SELECT e.category, SUM(e.amount)
            FROM Expense e
            WHERE e.user.id = :userId
            GROUP BY e.category
            """)
    List<Object[]> getCategoryWiseSpending(@Param("userId") Long userId);
    @Query("""
            SELECT MONTH(e.date), SUM(e.amount)
            FROM Expense e
            WHERE e.user.id = :userId
            GROUP BY MONTH(e.date)
            ORDER BY MONTH(e.date)
            """)
    List<Object[]> getMonthlySpending(@Param("userId") Long userId);
    @Query("""
            SELECT e
            FROM Expense e
            WHERE e.user.id = :userId
            AND (:category IS NULL OR e.category = :category)
            AND (:startDate IS NULL OR e.date >= :startDate)
            AND (:endDate IS NULL OR e.date <= :endDate)
            """)
    List<Expense> filterExpense(@Param("userId") Long userId,
                                @Param("category")ExpenseCategory expenseCategory,
                                @Param("startDate")LocalDate startDate,
                                @Param("endDate") LocalDate endDate);
}
