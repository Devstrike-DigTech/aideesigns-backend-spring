package com.aideesigns.backend.booking.controller

import com.aideesigns.backend.booking.dto.BookingResponse
import com.aideesigns.backend.booking.dto.BookingStatusUpdateRequest
import com.aideesigns.backend.booking.service.BookingService
import com.aideesigns.backend.shared.dto.ApiResponse
import com.aideesigns.backend.shared.enums.BookingStatus
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "Admin — Bookings", description = "Admin endpoints for managing booking requests")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/admin/bookings")
class BookingAdminController(
    private val bookingService: BookingService
) {

    @GetMapping
    @Operation(summary = "Get all bookings, newest first")
    fun getAllBookings(): ApiResponse<List<BookingResponse>> =
        ApiResponse.success(bookingService.getAllBookings())

    @GetMapping("/status/{status}")
    @Operation(summary = "Filter bookings by status")
    fun getByStatus(@PathVariable status: BookingStatus): ApiResponse<List<BookingResponse>> =
        ApiResponse.success(bookingService.getBookingsByStatus(status))

    @GetMapping("/{id}")
    @Operation(summary = "Get a single booking by ID")
    fun getById(@PathVariable id: UUID): ApiResponse<BookingResponse> =
        ApiResponse.success(bookingService.getBookingById(id))

    @PatchMapping("/{id}/status")
    @Operation(
        summary = "Update booking status",
        description = "Approve, decline, mark in-progress, or complete a booking. " +
                "Declining or cancelling automatically frees up the production slot capacity."
    )
    fun updateStatus(
        @PathVariable id: UUID,
        @Valid @RequestBody request: BookingStatusUpdateRequest
    ): ApiResponse<BookingResponse> =
        ApiResponse.success(
            bookingService.updateBookingStatus(id, request),
            "Booking status updated successfully"
        )
}
