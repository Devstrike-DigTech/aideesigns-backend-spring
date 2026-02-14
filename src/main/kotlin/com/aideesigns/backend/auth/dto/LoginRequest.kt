package com.aideesigns.backend.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@Schema(description = "Admin login credentials")
data class LoginRequest(

    @field:Email
    @field:NotBlank
    @Schema(example = "admin@aideesigns.com")
    val email: String,

    @field:NotBlank
    @Schema(example = "securepassword")
    val password: String
)
