package com.aideesigns.backend.booking.controller

import com.aideesigns.backend.booking.dto.ProductionSlotResponse
import com.aideesigns.backend.booking.service.ProductionSlotService
import com.aideesigns.backend.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Tag(name = "Public — Production Slots", description = "Public endpoints for viewing available production slots")
@RestController
@RequestMapping("/api/slots")
class ProductionSlotPublicController(
    private val productionSlotService: ProductionSlotService
) {

    @GetMapping
    @Operation(
        summary = "Get available production slots within a date range",
        description = "Returns production slots between the specified dates. Use this to show customers which dates are available for booking."
    )
    fun getSlots(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate
    ): ApiResponse<List<ProductionSlotResponse>> =
        ApiResponse.success(productionSlotService.getSlotsByDateRange(from, to))
}