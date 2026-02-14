package com.aideesigns.backend.booking.entity

import com.aideesigns.backend.shared.enums.BookingStatus
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "bookings")
class Booking(

    @Column(nullable = false)
    val customerName: String,

    @Column(nullable = false)
    val phone: String,

    @Column
    val email: String? = null,

    @Column(nullable = false)
    val outfitType: String,

    @Column
    val inspirationImageUrl: String? = null,

    @Column(columnDefinition = "TEXT")
    val notes: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_slot_id")
    var productionSlot: ProductionSlot? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: BookingStatus = BookingStatus.PENDING

) {
    @Id
    val id: UUID = UUID.randomUUID()

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
}
