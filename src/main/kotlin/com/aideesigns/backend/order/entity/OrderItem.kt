package com.aideesigns.backend.order.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "order_items")
class OrderItem(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @Column(nullable = false)
    val productId: UUID,

    @Column(nullable = false)
    val sizeLabel: String,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = false, precision = 12, scale = 2)
    val unitPrice: BigDecimal

) {
    @Id
    val id: UUID = UUID.randomUUID()

    val subtotal: BigDecimal
        get() = unitPrice.multiply(BigDecimal(quantity))
}
