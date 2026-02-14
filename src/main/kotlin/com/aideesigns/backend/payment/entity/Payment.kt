package com.aideesigns.backend.order.entity

import com.aideesigns.backend.shared.enums.PaymentGateway
import com.aideesigns.backend.shared.enums.PaymentStatus
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
@Table(name = "payments")
class Payment(

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val gateway: PaymentGateway,

    @Column
    var gatewayReference: String? = null,

    @Column(nullable = false, precision = 12, scale = 2)
    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PaymentStatus = PaymentStatus.PENDING,

    @Column
    var paidAt: Instant? = null

) {
    @Id
    val id: UUID = UUID.randomUUID()

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
}
