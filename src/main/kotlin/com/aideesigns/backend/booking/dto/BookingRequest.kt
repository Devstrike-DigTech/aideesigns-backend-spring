package com.aideesigns.backend.booking.dto

import com.aideesigns.backend.shared.enums.BookingStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate


// ─── Requests ──────────────────────────────────────────────────────────────

@Schema(description = "Payload for submitting a custom outfit booking")
data class BookingRequest(

    @field:NotBlank
    @Schema(example = "Amara Okonkwo")
    val customerName: String,

    @field:NotBlank
    @Schema(example = "+2348012345678")
    val phone: String,

    @field:Email
    @Schema(example = "amara@email.com")
    val email: String? = null,

    @field:NotBlank
    @Schema(example = "Ankara Two-Piece")
    val outfitType: String,

    @Schema(example = "https://res.cloudinary.com/aideesigns/inspiration.jpg")
    val inspirationImageUrl: String? = null,

    @Schema(example = "I want it fitted at the waist, knee length")
    val notes: String? = null,

    @field:NotNull
    @Schema(example = "2025-03-15")
    val preferredDate: LocalDate
)

@Schema(description = "Admin request to update booking status")
data class BookingStatusUpdateRequest(
    @field:NotNull
    val status: BookingStatus
)

@Schema(description = "Request to create or update a production slot")
data class ProductionSlotRequest(

    @field:NotNull
    @Schema(example = "2025-03-15")
    val productionDate: LocalDate,

    @field:NotNull
    @Schema(example = "3")
    val maxCapacity: Int,

    val isClosed: Boolean = false
)


