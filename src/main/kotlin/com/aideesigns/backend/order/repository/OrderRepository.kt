package com.aideesigns.backend.order.repository

import com.aideesigns.backend.order.entity.Order
import com.aideesigns.backend.shared.enums.FulfillmentStatus
import com.aideesigns.backend.shared.enums.PaymentStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface OrderRepository : JpaRepository<Order, UUID> {
    fun findAllByOrderByCreatedAtDesc(): List<Order>
    fun findAllByPaymentStatus(status: PaymentStatus): List<Order>
    fun findAllByFulfillmentStatus(status: FulfillmentStatus): List<Order>
    fun findByIdAndPhone(id: UUID, phone: String): Order?
    fun findByIdAndEmail(id: UUID, email: String): Order?
}


