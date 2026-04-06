package com.financeapp.dto;

import com.financeapp.enums.Role;
import lombok.Data;

@Data
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Boolean active;
}