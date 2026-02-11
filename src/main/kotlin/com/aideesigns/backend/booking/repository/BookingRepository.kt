package com.aideesigns.backend.booking.repository

import com.aideesigns.backend.booking.entity.Booking
import com.aideesigns.backend.shared.enums.BookingStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BookingRepository : JpaRepository<Booking, UUID> {
    fun findAllByOrderByCreatedAtDesc(): List<Booking>
    fun findAllByStatus(status: BookingStatus): List<Booking>
    fun findByIdAndEmail(id: UUID, email: String): Booking?
    fun findByIdAndPhone(id: UUID, phone: String): Booking?
}
