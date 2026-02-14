package com.aideesigns.backend.payment.service

import com.aideesigns.backend.config.PaystackConfig
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal


// ─── Paystack ──────────────────────────────────────────────────────────────

@Service("paystackGatewayService")
class PaystackGatewayService(
    private val config: PaystackConfig
) : PaymentGatewayService {

    private val restTemplate = RestTemplate()

    override fun initializePayment(request: PaymentInitRequest): PaymentInitResult {
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${config.secretKey}")
            contentType = MediaType.APPLICATION_JSON
        }

        // Paystack expects amount in kobo (multiply NGN by 100)
        val amountInKobo = request.amount.multiply(BigDecimal(100)).toLong()
        val reference = "ADS-${request.orderId}-${System.currentTimeMillis()}"

        val body = mapOf(
            "email" to request.email,
            "amount" to amountInKobo,
            "reference" to reference,
            "callback_url" to request.callbackUrl,
            "metadata" to mapOf("order_id" to request.orderId.toString())
        )

        val response = restTemplate.postForObject(
            "${config.baseUrl}/transaction/initialize",
            HttpEntity(body, headers),
            Map::class.java
        )

        val data = response?.get("data") as? Map<*, *>
            ?: throw RuntimeException("Failed to initialize Paystack payment")

        return PaymentInitResult(
            authorizationUrl = data["authorization_url"] as String,
            reference = data["reference"] as String
        )
    }

    override fun verifyPayment(reference: String): Boolean {
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${config.secretKey}")
        }

        val response = restTemplate.exchange(
            "${config.baseUrl}/transaction/verify/$reference",
            org.springframework.http.HttpMethod.GET,
            HttpEntity<Nothing>(headers),
            Map::class.java
        )

        val data = response.body?.get("data") as? Map<*, *> ?: return false
        return data["status"] == "success"
    }
}
