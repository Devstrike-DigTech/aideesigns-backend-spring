package com.aideesigns.backend.notification.email

import com.aideesigns.backend.booking.entity.Booking
import com.aideesigns.backend.order.entity.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class EmailTemplateService {

    private val dateFormatter = DateTimeFormatter
        .ofPattern("dd MMMM yyyy, hh:mm a")
        .withZone(ZoneId.of("Africa/Lagos"))

    private val dateOnlyFormatter = DateTimeFormatter
        .ofPattern("dd MMMM yyyy")

    fun buildBookingConfirmation(booking: Booking): String {
        val template = loadTemplate("booking-confirmation.html")
        return template
            .replace("{{BOOKING_ID}}", booking.id.toString())
            .replace("{{CUSTOMER_NAME}}", booking.customerName)
            .replace("{{OUTFIT_TYPE}}", booking.outfitType)
            .replace("{{PRODUCTION_DATE}}", booking.productionSlot?.productionDate
                ?.format(dateOnlyFormatter) ?: "To be confirmed")
            .replace("{{NOTES}}", booking.notes ?: "None")
            .replace("{{STATUS}}", booking.status.name)
            .replace("{{CREATED_AT}}", dateFormatter.format(booking.createdAt))
    }

    fun buildOrderConfirmation(order: Order): String {
        val template = loadTemplate("order-confirmation.html")
        val itemsHtml = order.items.joinToString("") { item ->
            """
            <tr>
                <td style="padding: 8px; border-bottom: 1px solid #eee;">${item.productId}</td>
                <td style="padding: 8px; border-bottom: 1px solid #eee;">${item.sizeLabel}</td>
                <td style="padding: 8px; border-bottom: 1px solid #eee;">${item.quantity}</td>
                <td style="padding: 8px; border-bottom: 1px solid #eee;">₦${item.unitPrice}</td>
                <td style="padding: 8px; border-bottom: 1px solid #eee;">₦${item.subtotal}</td>
            </tr>
            """.trimIndent()
        }

        return template
            .replace("{{ORDER_ID}}", order.id.toString())
            .replace("{{CUSTOMER_NAME}}", order.customerName)
            .replace("{{ORDER_ITEMS}}", itemsHtml)
            .replace("{{DELIVERY_FEE}}", "₦${order.deliveryAddress?.deliveryFee ?: "0.00"}")
            .replace("{{TOTAL_AMOUNT}}", "₦${order.totalAmount}")
            .replace("{{DELIVERY_ADDRESS}}", order.deliveryAddress?.let {
                "${it.addressLine}, ${it.city}, ${it.state}"
            } ?: "Not provided")
            .replace("{{PAYMENT_STATUS}}", order.paymentStatus.name)
            .replace("{{CREATED_AT}}", dateFormatter.format(order.createdAt))
    }

    fun buildPaymentConfirmation(order: Order): String {
        val template = loadTemplate("order-confirmation.html")
        return buildOrderConfirmation(order)
            .replace("{{PAYMENT_STATUS}}", "PAID ✓")
    }

    private fun loadTemplate(filename: String): String {
        val resource = ClassPathResource("templates/email/$filename")
        return resource.inputStream.readBytes().toString(StandardCharsets.UTF_8)
    }
}
