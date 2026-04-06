package com.financeapp.controller;

import com.financeapp.dto.*;
import com.financeapp.enums.TransactionType;
import com.financeapp.service.TransactionService;

import jakarta.validation.Valid;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TransactionResponseDto> create(@Valid @RequestBody TransactionRequestDto request) {
        TransactionResponseDto created = transactionService.createTransaction(request);
        return ApiResponse.success("Transaction created successfully", created);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ApiResponse<PageResponse<TransactionResponseDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        PageResponse<TransactionResponseDto> transactions = transactionService.getAllTransactions(page, size, sortBy, sortDir);
        return ApiResponse.success(transactions);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ApiResponse<PageResponse<TransactionResponseDto>> getTransactionsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        PageResponse<TransactionResponseDto> transactions = transactionService.getTransactionsByUser(userId, page, size, sortBy, sortDir);
        return ApiResponse.success(transactions);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ApiResponse<TransactionResponseDto> getById(@PathVariable Long id) {
        // For now, we'll implement this using the existing service
        // In a real implementation, you'd add a getById method to service
        throw new UnsupportedOperationException("Get by ID not implemented yet");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TransactionResponseDto> update(@PathVariable Long id,
            @Valid @RequestBody TransactionRequestDto request) {
        TransactionResponseDto updated = transactionService.updateTransaction(id, request);
        return ApiResponse.success("Transaction updated successfully", updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        return transactionService.deleteTransaction(id);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ApiResponse<PageResponse<TransactionResponseDto>> filter(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        PageResponse<TransactionResponseDto> transactions = transactionService.filterTransactions(
                type, categoryId, date, userId, page, size, sortBy, sortDir);
        return ApiResponse.success(transactions);
    }

    @GetMapping("/statistics/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public ApiResponse<java.util.Map<String, Object>> getStatistics(@PathVariable Long userId) {
        java.util.Map<String, Object> stats = transactionService.getTransactionStatistics(userId);
        return ApiResponse.success(stats);
    }
}
