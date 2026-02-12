package com.aideesigns.backend.notification.events

import com.aideesigns.backend.order.entity.Order
import org.springframework.context.ApplicationEvent


class PaymentConfirmedEvent(
    source: Any,
    val order: Order
) : ApplicationEvent(source)


