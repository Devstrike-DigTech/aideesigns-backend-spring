package com.aideesigns.backend.delivery.repository

import com.aideesigns.backend.order.entity.DeliveryAddress
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface DeliveryRepository : JpaRepository<DeliveryAddress, UUID> {
    fun findByOrderId(orderId: UUID): DeliveryAddress?
}


