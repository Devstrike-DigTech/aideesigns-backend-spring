package com.aideesigns.backend.auth.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@Schema(description = "Payload for registering the first admin account")
data class RegisterRequest(

    @field:Email
    @field:NotBlank
    @Schema(example = "admin@aideesigns.com")
    val email: String,

    @field:NotBlank
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(example = "securepassword123")
    val password: String,

    @Schema(description = "Setup key to prevent unauthorized registration")
    @field:NotBlank
    val setupKey: String
)
