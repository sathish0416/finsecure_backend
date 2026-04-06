package com.financeapp.service;

import com.financeapp.dto.LoginRequestDto;
import com.financeapp.dto.UserRequestDto;
import com.financeapp.dto.UserResponseDto;
import com.financeapp.exception.ApiException;
import com.financeapp.model.User;
import com.financeapp.repository.UserRepository;
import com.financeapp.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil; // 🔥 inject JWT

    // 🔹 Register User
    public UserResponseDto registerUser(UserRequestDto request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        if (user.getActive() == null) {
            user.setActive(true);
        }

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    // 🔹 Login User (NOW RETURNS JWT TOKEN 🔥)
    public String loginUser(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    // 🔹 Common Mapper
    private UserResponseDto mapToResponse(User user) {

        UserResponseDto response = new UserResponseDto();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setActive(user.getActive());

        return response;
    }
}