package com.financeapp.dto;

import java.math.BigDecimal;

public class CategorySummaryDto {

    private String category;
    private BigDecimal total;

    // Default constructor for Hibernate
    public CategorySummaryDto() {
    }

    // Constructor for JPQL query
    public CategorySummaryDto(String category, BigDecimal total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}