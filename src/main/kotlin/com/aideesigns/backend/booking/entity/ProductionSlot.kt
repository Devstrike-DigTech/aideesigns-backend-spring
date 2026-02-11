package com.aideesigns.backend.booking.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "production_slots")
class ProductionSlot(

    @Column(nullable = false, unique = true)
    val productionDate: LocalDate,

    @Column(nullable = false)
    var maxCapacity: Int,

    @Column(nullable = false)
    var bookedCount: Int = 0,

    @Column(nullable = false)
    var isClosed: Boolean = false

) {
    @Id
    val id: UUID = UUID.randomUUID()

    val isFull: Boolean
        get() = bookedCount >= maxCapacity

    val isAvailable: Boolean
        get() = !isClosed && !isFull

    fun incrementBookedCount() {
        bookedCount++
    }

    fun decrementBookedCount() {
        if (bookedCount > 0) bookedCount--
    }
}
