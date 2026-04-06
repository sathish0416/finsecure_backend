package com.financeapp.dto;

import com.financeapp.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDate date;
    private String notes;
    private CategoryDto category;
    private UserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TransactionResponseDto fromEntity(com.financeapp.model.Transaction transaction) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setDate(transaction.getDate());
        dto.setNotes(transaction.getNotes());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        
        if (transaction.getCategory() != null) {
            dto.setCategory(CategoryDto.fromEntity(transaction.getCategory()));
        }
        
        if (transaction.getUser() != null) {
            dto.setUser(UserDto.fromEntity(transaction.getUser()));
        }
        
        return dto;
    }
}
