package com.aideesigns.backend.auth.service

import com.aideesigns.backend.admin.service.AdminService
import com.aideesigns.backend.auth.dto.AuthResponse
import com.aideesigns.backend.auth.dto.LoginRequest
import com.aideesigns.backend.auth.dto.RegisterRequest
import com.aideesigns.backend.config.JwtConfig
import com.aideesigns.backend.shared.exception.DomainException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val adminService: AdminService,
    private val jwtService: JwtService,
    private val jwtConfig: JwtConfig,
    @Value("\${app.setup-key}") private val setupKey: String
) {

    fun login(request: LoginRequest): AuthResponse {
        val admin = adminService.findByEmail(request.email)

        if (!adminService.verifyPassword(request.password, admin.passwordHash)) {
            throw DomainException("Invalid email or password.")
        }

        val token = jwtService.generateToken(admin.email, admin.role)

        return AuthResponse(
            token = token,
            email = admin.email,
            role = admin.role,
            expiresIn = jwtConfig.expirationSeconds
        )
    }

    fun registerFirstAdmin(request: RegisterRequest): AuthResponse {
        if (request.setupKey != setupKey) {
            throw DomainException("Invalid setup key.")
        }

        val admin = adminService.registerFirstAdmin(
            email = request.email,
            rawPassword = request.password
        )

        val token = jwtService.generateToken(admin.email, admin.role)

        return AuthResponse(
            token = token,
            email = admin.email,
            role = admin.role,
            expiresIn = jwtConfig.expirationSeconds
        )
    }
}
