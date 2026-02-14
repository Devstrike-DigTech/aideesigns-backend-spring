package com.aideesigns.backend.payment.service

import com.aideesigns.backend.config.FlutterwaveConfig
import com.aideesigns.backend.config.PaystackConfig
import com.aideesigns.backend.order.repository.OrderRepository
import com.aideesigns.backend.payment.repository.PaymentRepository
import com.aideesigns.backend.shared.enums.FulfillmentStatus
import com.aideesigns.backend.shared.enums.PaymentStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class PaymentWebhookService(
    private val paymentRepository: PaymentRepository,
    private val orderRepository: OrderRepository,
    private val paystackConfig: PaystackConfig,
    private val flutterwaveConfig: FlutterwaveConfig
) {

    // ─── Paystack ──────────────────────────────────────────────────────────────

    fun verifyPaystackSignature(payload: String, signature: String): Boolean {
        val computed = hmacSha512(payload, paystackConfig.webhookSecret)
        return computed.equals(signature, ignoreCase = true)
    }

    @Transactional
    fun handlePaystackWebhook(payload: Map<String, Any>) {
        val event = payload["event"] as? String ?: return

        if (event != "charge.success") return

        val data = payload["data"] as? Map<*, *> ?: return
        val reference = data["reference"] as? String ?: return
        val status = data["status"] as? String ?: return

        if (status != "success") return

        markOrderAsPaid(reference)
    }

    // ─── Flutterwave ───────────────────────────────────────────────────────────

    fun verifyFlutterwaveSignature(signature: String): Boolean =
        signature == flutterwaveConfig.webhookSecret

    @Transactional
    fun handleFlutterwaveWebhook(payload: Map<String, Any>) {
        val event = payload["event"] as? String ?: return

        if (event != "charge.completed") return

        val data = payload["data"] as? Map<*, *> ?: return
        val reference = data["tx_ref"] as? String ?: return
        val status = data["status"] as? String ?: return

        if (status != "successful") return

        markOrderAsPaid(reference)
    }

    // ─── Shared ────────────────────────────────────────────────────────────────

    private fun markOrderAsPaid(reference: String) {
        val payment = paymentRepository.findByGatewayReference(reference) ?: return

        if (payment.status == PaymentStatus.PAID) return // Already processed

        payment.status = PaymentStatus.PAID
        payment.paidAt = Instant.now()
        paymentRepository.save(payment)

        val order = payment.order
        order.paymentStatus = PaymentStatus.PAID
        order.fulfillmentStatus = FulfillmentStatus.CONFIRMED
        order.updatedAt = Instant.now()
        orderRepository.save(order)
    }

    private fun hmacSha512(data: String, secret: String): String {
        val mac = Mac.getInstance("HmacSHA512")
        mac.init(SecretKeySpec(secret.toByteArray(), "HmacSHA512"))
        return mac.doFinal(data.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
