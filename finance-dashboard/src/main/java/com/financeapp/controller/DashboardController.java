package com.financeapp.controller;

import com.financeapp.dto.CategorySummaryDto;
import com.financeapp.dto.MonthlySummaryDto;
import com.financeapp.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public Map<String, BigDecimal> getSummary() {
        return transactionService.getDashboardSummary();
    }

    @GetMapping("/category-summary")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public List<CategorySummaryDto> getCategorySummary() {
        return transactionService.getCategorySummary();
    }

    @GetMapping("/monthly-trends")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST','VIEWER')")
    public List<MonthlySummaryDto> getMonthlyTrends() {
        return transactionService.getMonthlySummary();
    }
}