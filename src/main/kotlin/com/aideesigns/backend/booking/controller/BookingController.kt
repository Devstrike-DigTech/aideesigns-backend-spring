package com.aideesigns.backend.booking.controller

import com.aideesigns.backend.booking.dto.BookingRequest
import com.aideesigns.backend.booking.dto.BookingResponse
import com.aideesigns.backend.booking.dto.ProductionSlotResponse
import com.aideesigns.backend.booking.service.BookingService
import com.aideesigns.backend.booking.service.ProductionSlotService
import com.aideesigns.backend.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "Public — Bookings", description = "Public endpoints for submitting and tracking bookings")
@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService,
    private val productionSlotService: ProductionSlotService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Submit a custom outfit booking",
        description = "Creates a booking for the requested production date. Date must be at least 7 days in the future and the slot must have capacity."
    )
    fun createBooking(
        @Valid @RequestBody request: BookingRequest
    ): ApiResponse<BookingResponse> {
        val booking = bookingService.createBooking(request)
        return ApiResponse.success(
            booking,
            "Booking submitted successfully. Our team will contact you within 24 hours to confirm."
        )
    }

    @GetMapping("/track/{id}")
    @Operation(
        summary = "Track a booking",
        description = "Customer can track their booking using their booking ID plus phone or email."
    )
    fun trackBooking(
        @PathVariable id: UUID,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) email: String?
    ): ApiResponse<BookingResponse> =
        ApiResponse.success(bookingService.trackBooking(id, phone, email))

    @GetMapping("/available-dates")
    @Operation(
        summary = "Get available production dates",
        description = "Returns all open slots with remaining capacity at least 7 days ahead."
    )
    fun getAvailableDates(): ApiResponse<List<ProductionSlotResponse>> =
        ApiResponse.success(productionSlotService.getAvailableSlots())
}
