package com.aideesigns.backend.notification.listeners

import com.aideesigns.backend.config.MailConfig
import com.aideesigns.backend.notification.email.EmailService
import com.aideesigns.backend.notification.email.EmailTemplateService
import com.aideesigns.backend.notification.events.OrderCreatedEvent
import com.aideesigns.backend.notification.events.PaymentConfirmedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component


@Component
class OrderNotificationListener(
    private val emailService: EmailService,
    private val templateService: EmailTemplateService,
    private val mailConfig: MailConfig
) {

    @Async("emailTaskExecutor")
    @EventListener
    fun onOrderCreated(event: OrderCreatedEvent) {
        val order = event.order

        // Email customer
        order.email?.let { customerEmail ->
            val html = templateService.buildOrderConfirmation(order)
            emailService.sendHtml(
                to = customerEmail,
                subject = "Order Confirmed — Aideesigns & Stitches",
                htmlBody = html
            )
        }

        // Notify admin
        val html = templateService.buildOrderConfirmation(order)
        emailService.sendHtml(
            to = mailConfig.adminEmail,
            subject = "New Order from ${order.customerName} — ₦${order.totalAmount}",
            htmlBody = html
        )
    }

    @Async("emailTaskExecutor")
    @EventListener
    fun onPaymentConfirmed(event: PaymentConfirmedEvent) {
        val order = event.order

        // Email customer payment receipt
        order.email?.let { customerEmail ->
            val html = templateService.buildPaymentConfirmation(order)
            emailService.sendHtml(
                to = customerEmail,
                subject = "Payment Confirmed — Aideesigns & Stitches",
                htmlBody = html
            )
        }

        // Notify admin
        emailService.sendHtml(
            to = mailConfig.adminEmail,
            subject = "Payment Received: Order ${order.id} — ₦${order.totalAmount}",
            htmlBody = templateService.buildPaymentConfirmation(order)
        )
    }
}