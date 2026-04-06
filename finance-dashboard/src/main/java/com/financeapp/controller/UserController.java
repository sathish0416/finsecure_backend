package com.financeapp.controller;

import com.financeapp.dto.LoginRequestDto;
import com.financeapp.dto.UserRequestDto;
import com.financeapp.dto.UserResponseDto;
import com.financeapp.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResponseDto register(@Valid @RequestBody UserRequestDto request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequestDto request) {
        return userService.loginUser(request);
    }

    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {
        return "Admin access granted";
    }

    @GetMapping("/analyst/test")
    @PreAuthorize("hasAnyRole('ADMIN','ANALYST')")
    public String analystTest() {
        return "Analyst access granted";
    }
}