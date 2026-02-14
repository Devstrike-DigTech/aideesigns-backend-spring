package com.aideesigns.backend.order.dto

import com.aideesigns.backend.shared.enums.FulfillmentStatus
import com.aideesigns.backend.shared.enums.PaymentGateway
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.util.*


// ─── Requests ──────────────────────────────────────────────────────────────

@Schema(description = "A single item in the order")
data class OrderItemRequest(

    @field:NotNull
    val productId: UUID,

    @field:NotBlank
    @Schema(example = "XL")
    val sizeLabel: String,

    @field:Min(1)
    val quantity: Int
)

@Schema(description = "Delivery address for the order")
data class DeliveryAddressRequest(

    @field:NotBlank
    @Schema(example = "12 Adeola Odeku Street, Victoria Island")
    val addressLine: String,

    @field:NotBlank
    @Schema(example = "Lagos")
    val city: String,

    @field:NotBlank
    @Schema(example = "Lagos State")
    val state: String,

    @field:NotBlank
    @Schema(example = "+2348012345678")
    val contactPhone: String
)

@Schema(description = "Payload for creating an order")
data class CreateOrderRequest(

    @field:NotBlank
    @Schema(example = "Amara Okonkwo")
    val customerName: String,

    @field:NotBlank
    @Schema(example = "+2348012345678")
    val phone: String,

    @field:Email
    @Schema(example = "amara@email.com")
    val email: String? = null,

    @field:NotEmpty
    @field:Valid
    val items: List<OrderItemRequest>,

    @field:Valid
    @field:NotNull
    val deliveryAddress: DeliveryAddressRequest,

    @field:NotNull
    @Schema(description = "Which gateway to pay with", example = "PAYSTACK")
    val gateway: PaymentGateway
)

@Schema(description = "Admin request to update fulfillment status")
data class FulfillmentStatusUpdateRequest(
    @field:NotNull
    val status: FulfillmentStatus
)


