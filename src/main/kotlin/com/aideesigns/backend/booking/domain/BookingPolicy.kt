package com.aideesigns.backend.booking.domain

import com.aideesigns.backend.shared.exception.DomainException
import java.time.LocalDate

object BookingPolicy {

    private const val MIN_DAYS_AHEAD = 7

    fun validateBookingDate(requestedDate: LocalDate) {
        val today = LocalDate.now()
        val earliestAllowed = today.plusDays(MIN_DAYS_AHEAD.toLong())

        if (requestedDate.isBefore(today)) {
            throw DomainException("Booking date cannot be in the past.")
        }

        if (requestedDate.isBefore(earliestAllowed)) {
            throw DomainException(
                "Bookings require at least $MIN_DAYS_AHEAD days notice. " +
                "Earliest available date is $earliestAllowed."
            )
        }
    }
}
