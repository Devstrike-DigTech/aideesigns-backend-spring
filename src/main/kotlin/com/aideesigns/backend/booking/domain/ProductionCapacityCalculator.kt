package com.aideesigns.backend.booking.domain

import com.aideesigns.backend.booking.entity.ProductionSlot
import com.aideesigns.backend.shared.exception.DomainException

object ProductionCapacityCalculator {

    fun assertSlotAvailable(slot: ProductionSlot) {
        if (slot.isClosed) {
            throw DomainException(
                "The selected date (${slot.productionDate}) is closed for bookings."
            )
        }
        if (slot.isFull) {
            throw DomainException(
                "The selected date (${slot.productionDate}) is fully booked. " +
                "Please choose a different date."
            )
        }
    }

    fun remainingCapacity(slot: ProductionSlot): Int =
        (slot.maxCapacity - slot.bookedCount).coerceAtLeast(0)
}
