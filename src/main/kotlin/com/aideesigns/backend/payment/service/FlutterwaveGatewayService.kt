package com.aideesigns.backend.payment.service

import com.aideesigns.backend.config.FlutterwaveConfig
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


// ─── Flutterwave ───────────────────────────────────────────────────────────

@Service("flutterwaveGatewayService")
class FlutterwaveGatewayService(
    private val config: FlutterwaveConfig
) : PaymentGatewayService {

    private val restTemplate = RestTemplate()

    override fun initializePayment(request: PaymentInitRequest): PaymentInitResult {
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${config.secretKey}")
            contentType = MediaType.APPLICATION_JSON
        }

        val reference = "ADS-FLW-${request.orderId}-${System.currentTimeMillis()}"

        val body = mapOf(
            "tx_ref" to reference,
            "amount" to request.amount,
            "currency" to "NGN",
            "redirect_url" to request.callbackUrl,
            "customer" to mapOf(
                "email" to request.email,
                "phonenumber" to "",
                "name" to ""
            ),
            "meta" to mapOf("order_id" to request.orderId.toString()),
            "customizations" to mapOf(
                "title" to "Aideesigns & Stitches",
                "description" to "Payment for order ${request.orderId}"
            )
        )

        val response = restTemplate.postForObject(
            "${config.baseUrl}/payments",
            HttpEntity(body, headers),
            Map::class.java
        )

        val data = response?.get("data") as? Map<*, *>
            ?: throw RuntimeException("Failed to initialize Flutterwave payment")

        return PaymentInitResult(
            authorizationUrl = data["link"] as String,
            reference = reference
        )
    }

    override fun verifyPayment(reference: String): Boolean {
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${config.secretKey}")
        }

        val response = restTemplate.exchange(
            "${config.baseUrl}/transactions/verify_by_reference?tx_ref=$reference",
            org.springframework.http.HttpMethod.GET,
            HttpEntity<Nothing>(headers),
            Map::class.java
        )

        val data = response.body?.get("data") as? Map<*, *> ?: return false
        return data["status"] == "successful"
    }
}