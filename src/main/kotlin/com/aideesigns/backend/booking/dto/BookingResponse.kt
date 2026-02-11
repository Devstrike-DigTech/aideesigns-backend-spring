package com.aideesigns.backend.booking.dto

import com.aideesigns.backend.shared.enums.BookingStatus
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.Instant
import java.util.*

// ─── Responses ─────────────────────────────────────────────────────────────

@Schema(description = "Booking details returned to client")
data class BookingResponse(
    val id: UUID,
    val customerName: String,
    val phone: String,
    val email: String?,
    val outfitType: String,
    val inspirationImageUrl: String?,
    val notes: String?,
    val status: BookingStatus,
    val productionDate: LocalDate?,
    val createdAt: Instant
)

@Schema(description = "Production slot details")
data class ProductionSlotResponse(
    val id: UUID,
    val productionDate: LocalDate,
    val maxCapacity: Int,
    val bookedCount: Int,
    val remainingCapacity: Int,
    val isClosed: Boolean,
    val isAvailable: Boolean
)


