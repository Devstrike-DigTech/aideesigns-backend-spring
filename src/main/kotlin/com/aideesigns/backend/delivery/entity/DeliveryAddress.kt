package com.aideesigns.backend.order.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "delivery_addresses")
class DeliveryAddress(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @Column(nullable = false, columnDefinition = "TEXT")
    val addressLine: String,

    @Column(nullable = false)
    val city: String,

    @Column(nullable = false)
    val state: String,

    @Column(nullable = false, precision = 10, scale = 2)
    val deliveryFee: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false)
    val contactPhone: String

) {
    @Id
    val id: UUID = UUID.randomUUID()
}
