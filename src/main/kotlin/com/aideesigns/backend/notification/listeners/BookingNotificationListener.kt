package com.aideesigns.backend.notification.listeners

import com.aideesigns.backend.config.MailConfig
import com.aideesigns.backend.notification.email.EmailService
import com.aideesigns.backend.notification.email.EmailTemplateService
import com.aideesigns.backend.notification.events.BookingCreatedEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component


@Component
class BookingNotificationListener(
    private val emailService: EmailService,
    private val templateService: EmailTemplateService,
    private val mailConfig: MailConfig
) {

    @Async("emailTaskExecutor")
    @EventListener
    fun onBookingCreated(event: BookingCreatedEvent) {
        val booking = event.booking

        // Email customer if they provided an email
        booking.email?.let { customerEmail ->
            val html = templateService.buildBookingConfirmation(booking)
            emailService.sendHtml(
                to = customerEmail,
                subject = "Booking Received — Aideesigns & Stitches",
                htmlBody = html
            )
        }

        // Always notify admin
        val html = templateService.buildBookingConfirmation(booking)
        emailService.sendHtml(
            to = mailConfig.adminEmail,
            subject = "New Booking: ${booking.outfitType} from ${booking.customerName}",
            htmlBody = html
        )
    }
}


