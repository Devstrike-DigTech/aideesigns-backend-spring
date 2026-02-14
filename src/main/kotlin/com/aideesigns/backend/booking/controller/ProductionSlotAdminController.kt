package com.aideesigns.backend.booking.controller

import com.aideesigns.backend.booking.dto.ProductionSlotRequest
import com.aideesigns.backend.booking.dto.ProductionSlotResponse
import com.aideesigns.backend.booking.service.ProductionSlotService
import com.aideesigns.backend.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@Tag(name = "Admin — Production Slots", description = "Admin endpoints for managing production slot availability")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/admin/slots")
class ProductionSlotAdminController(
    private val productionSlotService: ProductionSlotService
) {

    @GetMapping
    @Operation(summary = "Get all slots within a date range")
    fun getSlots(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate
    ): ApiResponse<List<ProductionSlotResponse>> =
        ApiResponse.success(productionSlotService.getSlotsByDateRange(from, to))

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new production slot")
    fun createSlot(
        @Valid @RequestBody request: ProductionSlotRequest
    ): ApiResponse<ProductionSlotResponse> =
        ApiResponse.success(
            productionSlotService.createSlot(request),
            "Production slot created successfully"
        )

    @PutMapping("/{id}")
    @Operation(summary = "Update a production slot's capacity or closed status")
    fun updateSlot(
        @PathVariable id: UUID,
        @Valid @RequestBody request: ProductionSlotRequest
    ): ApiResponse<ProductionSlotResponse> =
        ApiResponse.success(
            productionSlotService.updateSlot(id, request),
            "Production slot updated successfully"
        )

    @PatchMapping("/{id}/toggle-closed")
    @Operation(summary = "Toggle a slot open or closed")
    fun toggleClosed(@PathVariable id: UUID): ApiResponse<ProductionSlotResponse> =
        ApiResponse.success(
            productionSlotService.toggleSlotClosed(id),
            "Slot availability toggled"
        )

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a production slot")
    fun deleteSlot(@PathVariable id: UUID): ApiResponse<Nothing> {
        productionSlotService.deleteSlot(id)
        return ApiResponse.success("Production slot deleted successfully")
    }
}
