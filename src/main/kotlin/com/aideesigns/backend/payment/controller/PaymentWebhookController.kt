package com.aideesigns.backend.payment.controller

import com.aideesigns.backend.payment.service.PaymentWebhookService
import com.aideesigns.backend.shared.exception.DomainException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "Webhooks", description = "Payment gateway webhook receivers")
@RestController
@RequestMapping("/api/payments/webhook")
class PaymentWebhookController(
    private val webhookService: PaymentWebhookService
) {

  /*  @PostMapping("/paystack")
    @Operation(
        summary = "Paystack webhook receiver",
        description = "Receives charge.success events from Paystack and marks orders as paid."
    )
    fun paystackWebhook(
        @RequestHeader("x-paystack-signature") signature: String,
        @RequestBody payload: String,
        @RequestBody parsedPayload: Map<String, Any>
    ): ResponseEntity<String> {
        if (!webhookService.verifyPaystackSignature(payload, signature)) {
            throw DomainException("Invalid Paystack webhook signature.")
        }
        webhookService.handlePaystackWebhook(parsedPayload)
        return ResponseEntity.ok("OK")
    }*/

    @PostMapping("/paystack")
    @Operation(summary = "Paystack webhook receiver")
    fun paystackWebhook(
        @RequestHeader("x-paystack-signature") signature: String,
        @RequestBody rawBody: ByteArray
    ): ResponseEntity<String> {
        val rawPayload = String(rawBody, Charsets.UTF_8)

        if (!webhookService.verifyPaystackSignature(rawPayload, signature)) {
            throw DomainException("Invalid Paystack webhook signature.")
        }

        val payload = com.fasterxml.jackson.databind.ObjectMapper()
            .readValue(rawPayload, Map::class.java) as Map<String, Any>

        webhookService.handlePaystackWebhook(payload)
        return ResponseEntity.ok("OK")
    }

    @PostMapping("/flutterwave")
    @Operation(
        summary = "Flutterwave webhook receiver",
        description = "Receives charge.completed events from Flutterwave and marks orders as paid."
    )
    fun flutterwaveWebhook(
        @RequestHeader("verif-hash") signature: String,
        @RequestBody payload: Map<String, Any>
    ): ResponseEntity<String> {
        if (!webhookService.verifyFlutterwaveSignature(signature)) {
            throw DomainException("Invalid Flutterwave webhook signature.")
        }
        webhookService.handleFlutterwaveWebhook(payload)
        return ResponseEntity.ok("OK")
    }
}
