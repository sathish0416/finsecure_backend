package com.financeapp.repository;

import com.financeapp.dto.CategorySummaryDto;
import com.financeapp.enums.TransactionType;
import com.financeapp.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(:type IS NULL OR t.type = :type) AND " +
            "(:categoryId IS NULL OR t.category.id = :categoryId) AND " +
            "(cast(:date as date) IS NULL OR t.date = :date) AND " +
            "(:userId IS NULL OR t.user.id = :userId)")
    Page<Transaction> filterTransactions(
            @Param("type") TransactionType type,
            @Param("categoryId") Long categoryId,
            @Param("date") LocalDate date,
            @Param("userId") Long userId,
            Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'INCOME'")
    BigDecimal getTotalIncome();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE'")
    BigDecimal getTotalExpense();

    @Query("SELECT new com.financeapp.dto.CategorySummaryDto(c.name, SUM(t.amount)) " +
            "FROM Transaction t JOIN t.category c " +
            "GROUP BY c.name")
    List<CategorySummaryDto> getCategorySummary();

    @Query(value = "SELECT TO_CHAR(date, 'YYYY-MM') AS month, SUM(amount) AS total " +
            "FROM transactions " +
            "GROUP BY month " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> getMonthlySummaryRaw();

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND t.date BETWEEN :startDate AND :endDate")
    Page<Transaction> findByUserIdAndDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}