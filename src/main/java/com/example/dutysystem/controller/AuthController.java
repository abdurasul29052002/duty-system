package com.example.dutysystem.controller;

import com.example.dutysystem.entity.User;
import com.example.dutysystem.payload.ApiResponse;
import com.example.dutysystem.payload.LoginDto;
import com.example.dutysystem.payload.RegisterDto;
import com.example.dutysystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> registerUser(@RequestBody RegisterDto registerDto) {
        ApiResponse apiResponse = authService.registerUser(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PostMapping("/login")
    public HttpEntity<?> loginUser(@RequestBody LoginDto loginDto, HttpServletResponse httpServletResponse) {
        ApiResponse apiResponse = authService.loginUser(loginDto, httpServletResponse);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
