package com.aideesigns.backend.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Returned after successful authentication")
data class AuthResponse(

    @Schema(description = "JWT bearer token")
    val token: String,

    @Schema(description = "Admin email address")
    val email: String,

    @Schema(description = "Admin role", example = "ADMIN")
    val role: String,

    @Schema(description = "Token expiry in seconds", example = "86400")
    val expiresIn: Long
)
