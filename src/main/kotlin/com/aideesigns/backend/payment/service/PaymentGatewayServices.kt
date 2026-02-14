package com.aideesigns.backend.payment.service

import com.aideesigns.backend.config.FlutterwaveConfig
import com.aideesigns.backend.config.PaystackConfig
import com.aideesigns.backend.shared.enums.PaymentGateway
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.util.*

// ─── Shared DTOs ───────────────────────────────────────────────────────────

data class PaymentInitRequest(
    val orderId: UUID,
    val email: String,
    val amount: BigDecimal,
    val gateway: PaymentGateway,
    val callbackUrl: String
)

data class PaymentInitResult(
    val authorizationUrl: String,
    val reference: String
)

// ─── Gateway Interface ─────────────────────────────────────────────────────

interface PaymentGatewayService {
    fun initializePayment(request: PaymentInitRequest): PaymentInitResult
    fun verifyPayment(reference: String): Boolean
}

