package com.aideesigns.backend.order.controller

import com.aideesigns.backend.order.dto.*
import com.aideesigns.backend.order.service.OrderService
import com.aideesigns.backend.shared.dto.ApiResponse
import com.aideesigns.backend.shared.enums.FulfillmentStatus
import com.aideesigns.backend.shared.enums.PaymentStatus
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "Public — Orders", description = "Public endpoints for placing and tracking orders")
@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Place a new order",
        description = "Creates an order, calculates delivery fee, initializes payment, and returns the payment URL."
    )
    fun createOrder(
        @Valid @RequestBody request: CreateOrderRequest
    ): ApiResponse<CreateOrderResponse> =
        ApiResponse.success(
            orderService.createOrder(request),
            "Order created. Complete your payment using the provided URL."
        )

    @GetMapping("/track/{id}")
    @Operation(
        summary = "Track an order",
        description = "Customer tracks their order using order ID plus phone or email."
    )
    fun trackOrder(
        @PathVariable id: UUID,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) email: String?
    ): ApiResponse<OrderResponse> =
        ApiResponse.success(orderService.trackOrder(id, phone, email))
}
