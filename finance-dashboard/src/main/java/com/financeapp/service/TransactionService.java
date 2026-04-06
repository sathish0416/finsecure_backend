package com.financeapp.service;

import com.financeapp.dto.*;
import com.financeapp.enums.TransactionType;
import com.financeapp.exception.*;
import com.financeapp.model.*;
import com.financeapp.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public TransactionResponseDto createTransaction(TransactionRequestDto request) {
        // Validate business rules
        validateTransactionRequest(request);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));

        // Additional business validation
        validateUserCanCreateTransaction(user, request);

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setNotes(request.getNotes());
        transaction.setCategory(category);
        transaction.setUser(user);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionResponseDto.fromEntity(savedTransaction);
    }

    public PageResponse<TransactionResponseDto> getAllTransactions(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = createPageable(page, size, sortBy, sortDir);
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        
        return PageResponse.of(transactionPage.map(TransactionResponseDto::fromEntity));
    }

    public PageResponse<TransactionResponseDto> getTransactionsByUser(Long userId, int page, int size, String sortBy, String sortDir) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
                
        Pageable pageable = createPageable(page, size, sortBy, sortDir);
        Page<Transaction> transactionPage = transactionRepository.findByUserId(userId, pageable);
        
        return PageResponse.of(transactionPage.map(TransactionResponseDto::fromEntity));
    }

    public TransactionResponseDto updateTransaction(Long id, TransactionRequestDto request) {
        // Validate business rules
        validateTransactionRequest(request);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", id));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.getCategoryId()));

        // Validate ownership
        if (!transaction.getUser().getId().equals(request.getUserId())) {
            throw new BusinessException("Transaction can only be updated by the owning user");
        }

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setNotes(request.getNotes());
        transaction.setCategory(category);

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return TransactionResponseDto.fromEntity(updatedTransaction);
    }

    public ApiResponse<Void> deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", id));

        // Additional validation: can only delete recent transactions (e.g., within last 90 days)
        if (transaction.getDate().isBefore(LocalDate.now().minusDays(90))) {
            throw new BusinessException("Cannot delete transactions older than 90 days");
        }

        transactionRepository.delete(transaction);
        return ApiResponse.success("Transaction deleted successfully", null);
    }

    public PageResponse<TransactionResponseDto> filterTransactions(
            TransactionType type,
            Long categoryId,
            LocalDate date,
            Long userId,
            int page,
            int size,
            String sortBy,
            String sortDir) {
        
        Pageable pageable = createPageable(page, size, sortBy, sortDir);
        Page<Transaction> transactionPage = transactionRepository.filterTransactions(
                type, categoryId, date, userId, pageable);
        
        return PageResponse.of(transactionPage.map(TransactionResponseDto::fromEntity));
    }

    public Map<String, BigDecimal> getDashboardSummary() {
        BigDecimal income = transactionRepository.getTotalIncome();
        BigDecimal expense = transactionRepository.getTotalExpense();

        BigDecimal balance = income.subtract(expense);

        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("totalIncome", income);
        summary.put("totalExpense", expense);
        summary.put("netBalance", balance);

        return summary;
    }

    public List<CategorySummaryDto> getCategorySummary() {
        return transactionRepository.getCategorySummary();
    }

    public List<MonthlySummaryDto> getMonthlySummary() {
        List<Object[]> results = transactionRepository.getMonthlySummaryRaw();

        List<MonthlySummaryDto> response = new ArrayList<>();

        for (Object[] row : results) {
            String month = (String) row[0];
            BigDecimal total = new BigDecimal(((Number) row[1]).doubleValue());

            response.add(new MonthlySummaryDto(month, total));
        }

        return response;
    }

    public Map<String, Object> getTransactionStatistics(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        
        long totalTransactions = transactionRepository.countByUserId(userId);
        stats.put("totalTransactions", totalTransactions);
        
        // Get recent transactions count (last 30 days)
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Transaction> recentTransactions = transactionRepository.findByUserIdAndDateBetween(
                userId, thirtyDaysAgo, LocalDate.now(), pageable);
        stats.put("recentTransactions", recentTransactions.getTotalElements());
        
        return stats;
    }

    // Private helper methods
    private void validateTransactionRequest(TransactionRequestDto request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Transaction amount must be positive");
        }

        if (request.getDate().isAfter(LocalDate.now())) {
            throw new BusinessException("Transaction date cannot be in the future");
        }

        // Additional business rule: limit daily transaction amount
        if (request.getAmount().compareTo(new BigDecimal("1000000")) > 0) {
            throw new BusinessException("Transaction amount exceeds daily limit");
        }
    }

    private void validateUserCanCreateTransaction(User user, TransactionRequestDto request) {
        if (!user.getActive()) {
            throw new BusinessException("Inactive users cannot create transactions");
        }

        // Additional business validation based on user role
        switch (user.getRole()) {
            case VIEWER:
                throw new BusinessException("Viewers cannot create transactions");
            case ANALYST:
                // Analysts can create transactions but with lower limits
                if (request.getAmount().compareTo(new BigDecimal("10000")) > 0) {
                    throw new BusinessException("Analysts cannot create transactions exceeding $10,000");
                }
                break;
            case ADMIN:
                // Admins have no restrictions
                break;
        }
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDir) {
        // Validate page parameters
        if (page < 0) page = 0;
        if (size < 1 || size > 100) size = 20; // Default size and max limit
        
        // Default sort
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "createdAt";
        }
        if (sortDir == null || sortDir.trim().isEmpty()) {
            sortDir = "desc";
        }

        return PageRequest.of(page, size, 
            org.springframework.data.domain.Sort.Direction.fromString(sortDir), sortBy);
    }
}