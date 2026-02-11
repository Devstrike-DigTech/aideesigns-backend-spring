package com.aideesigns.backend.auth.controller

import com.aideesigns.backend.auth.dto.AuthResponse
import com.aideesigns.backend.auth.dto.LoginRequest
import com.aideesigns.backend.auth.dto.RegisterRequest
import com.aideesigns.backend.auth.service.AuthService
import com.aideesigns.backend.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication and admin setup")
class AuthController(private val authService: AuthService) {

    @PostMapping("/login")
    @Operation(summary = "Admin login", description = "Returns a JWT token on success")
    fun login(@Valid @RequestBody request: LoginRequest): ApiResponse<AuthResponse> {
        val response = authService.login(request)
        return ApiResponse.success(response, "Login successful")
    }

    @PostMapping("/setup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Register first admin",
        description = "One-time endpoint. Fails if an admin already exists. Requires setup key from environment."
    )
    fun setup(@Valid @RequestBody request: RegisterRequest): ApiResponse<AuthResponse> {
        val response = authService.registerFirstAdmin(request)
        return ApiResponse.success(response, "Admin account created successfully")
    }
}
