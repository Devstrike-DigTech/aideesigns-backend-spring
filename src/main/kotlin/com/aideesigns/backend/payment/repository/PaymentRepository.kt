package com.aideesigns.backend.payment.repository

import com.aideesigns.backend.order.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PaymentRepository : JpaRepository<Payment, UUID> {
    fun findByOrderId(orderId: UUID): Payment?
    fun findByGatewayReference(reference: String): Payment?
}


