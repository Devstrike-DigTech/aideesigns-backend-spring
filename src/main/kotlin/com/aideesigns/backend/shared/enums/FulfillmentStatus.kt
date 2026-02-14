package com.aideesigns.backend.shared.enums

enum class FulfillmentStatus {
    PENDING,        // Order placed, not yet confirmed
    CONFIRMED,      // Admin confirmed the order
    PROCESSING,     // Being prepared
    SHIPPED,        // On the way
    DELIVERED,      // Delivered to customer
    CANCELLED       // Order cancelled
}

enum class PaymentGateway {
    PAYSTACK,
    FLUTTERWAVE,
    MANUAL           // Admin manually confirmed payment
}
