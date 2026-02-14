package com.aideesigns.backend.notification.events

import com.aideesigns.backend.booking.entity.Booking
import org.springframework.context.ApplicationEvent

class BookingCreatedEvent(
    source: Any,
    val booking: Booking
) : ApplicationEvent(source)


