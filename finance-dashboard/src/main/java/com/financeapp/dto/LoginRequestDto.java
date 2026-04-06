package com.financeapp.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequestDto {

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}