package com.aideesigns.backend.order.controller

import com.aideesigns.backend.order.dto.FulfillmentStatusUpdateRequest
import com.aideesigns.backend.order.dto.OrderResponse
import com.aideesigns.backend.order.service.OrderService
import com.aideesigns.backend.shared.dto.ApiResponse
import com.aideesigns.backend.shared.enums.FulfillmentStatus
import com.aideesigns.backend.shared.enums.PaymentStatus
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.*


@Tag(name = "Admin — Orders", description = "Admin endpoints for managing orders")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/admin/orders")
class OrderAdminController(private val orderService: OrderService) {

    @GetMapping
    @Operation(summary = "Get all orders, newest first")
    fun getAllOrders(): ApiResponse<List<OrderResponse>> =
        ApiResponse.success(orderService.getAllOrders())

    @GetMapping("/{id}")
    @Operation(summary = "Get a single order by ID")
    fun getById(@PathVariable id: UUID): ApiResponse<OrderResponse> =
        ApiResponse.success(orderService.getOrderById(id))

    @GetMapping("/payment-status/{status}")
    @Operation(summary = "Filter orders by payment status")
    fun getByPaymentStatus(
        @PathVariable status: PaymentStatus
    ): ApiResponse<List<OrderResponse>> =
        ApiResponse.success(orderService.getOrdersByPaymentStatus(status))

    @GetMapping("/fulfillment-status/{status}")
    @Operation(summary = "Filter orders by fulfillment status")
    fun getByFulfillmentStatus(
        @PathVariable status: FulfillmentStatus
    ): ApiResponse<List<OrderResponse>> =
        ApiResponse.success(orderService.getOrdersByFulfillmentStatus(status))

    @PatchMapping("/{id}/fulfillment")
    @Operation(summary = "Update order fulfillment status")
    fun updateFulfillment(
        @PathVariable id: UUID,
        @Valid @RequestBody request: FulfillmentStatusUpdateRequest
    ): ApiResponse<OrderResponse> =
        ApiResponse.success(
            orderService.updateFulfillmentStatus(id, request),
            "Fulfillment status updated"
        )

    @PostMapping("/{id}/confirm-payment")
    @Operation(
        summary = "Manually confirm payment",
        description = "Admin override — marks order as paid without gateway verification."
    )
    fun confirmPayment(@PathVariable id: UUID): ApiResponse<OrderResponse> =
        ApiResponse.success(
            orderService.manualPaymentConfirm(id),
            "Payment confirmed manually"
        )
}
