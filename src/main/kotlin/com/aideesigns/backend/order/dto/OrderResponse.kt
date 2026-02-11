package com.aideesigns.backend.order.dto

import com.aideesigns.backend.shared.enums.FulfillmentStatus
import com.aideesigns.backend.shared.enums.PaymentGateway
import com.aideesigns.backend.shared.enums.PaymentStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.Instant
import java.util.*


// ─── Responses ─────────────────────────────────────────────────────────────

data class OrderItemResponse(
    val id: UUID,
    val productId: UUID,
    val sizeLabel: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val subtotal: BigDecimal
)

data class DeliveryAddressResponse(
    val addressLine: String,
    val city: String,
    val state: String,
    val deliveryFee: BigDecimal,
    val contactPhone: String
)

data class PaymentResponse(
    val id: UUID,
    val gateway: PaymentGateway,
    val gatewayReference: String?,
    val amount: BigDecimal,
    val status: PaymentStatus,
    val paidAt: Instant?,
    val createdAt: Instant
)

@Schema(description = "Full order response")
data class OrderResponse(
    val id: UUID,
    val customerName: String,
    val phone: String,
    val email: String?,
    val totalAmount: BigDecimal,
    val paymentStatus: PaymentStatus,
    val fulfillmentStatus: FulfillmentStatus,
    val items: List<OrderItemResponse>,
    val deliveryAddress: DeliveryAddressResponse?,
    val payment: PaymentResponse?,
    val createdAt: Instant,
    val updatedAt: Instant
)

@Schema(description = "Returned after order creation with payment initiation details")
data class CreateOrderResponse(
    val order: OrderResponse,
    val paymentAuthorizationUrl: String,   // URL to redirect customer to for payment
    val paymentReference: String           // Reference to pass back on webhook
)


